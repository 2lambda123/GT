package com.bifrost.asgard.cache;

import com.bifrost.asgard.enums.BookType;
import com.bifrost.common.view.OrderView;
import lombok.extern.java.Log;
import redis.clients.jedis.Jedis;

import java.util.*;

@Log
public class Tesseract implements InfinityStone {

    private String underlier;
    private String bookType;

    private final Jedis jedis;

    // These 4 must always be affected together
    private double amountAvailable;
    private TreeMap<Double, Long> numberOfSharesPerPrice;
    private Map<Double, Map<Long, OrderView>> ordersGroupedByPrice;
    private Map<Long, OrderView> cache;

    // This one may not
    private List<OrderView> completedOrders;

    public Tesseract(BookType bookType, String underlier) {
        this.underlier = underlier;

        this.amountAvailable = 0;
        this.ordersGroupedByPrice = new HashMap<>();
        this.completedOrders = new ArrayList<>();
        this.cache = new HashMap<>();

        if (bookType == BookType.BID) {
            this.bookType = "bids";
            this.numberOfSharesPerPrice = new TreeMap<>(Collections.reverseOrder());
        } else {
            this.bookType = "asks";
            this.numberOfSharesPerPrice = new TreeMap<>();
        }

        jedis = new Jedis("redis");
        jedis.set("available","0");
        System.out.println("Amount in REDIS (Constructor): " + jedis.get("available"));
    }

    // underlier_book_variable_{firstLevelKey}_{secondLevelKey}  {} = Optional

    // Amount Available
    // spx:bids:aa (Double)
    // spx:asks:aa (Double)
    public String generateAmountAvailableKey() {
        return getUnderlier() + ":" + getBookType() + ":" + "aa";
    }

    // Number of Shares Per Price
    // spx:bids:spp:100.0 (Long)
    // spx:asks:spp:100.0 (Long)
    public String generateSharesPerPriceKey(double price) {
        return getUnderlier() + ":" + getBookType() + ":spp:" + price;
    }

    // Orders Grouped By Price
    // spx:bids:ogp:100.0:42 (OrderView)
    // spx:asks:ogp:100.0:42 (OrderView)
    public String generateOrdersGroupedByPriceKey(OrderView order) {
        return getUnderlier() + ":" + getBookType() + ":ogp:" + order.getPrice() + ":" + order.getId();
    }

    // Cache
    // spx:bids:cache:42 (OrderView)
    // spx:asks:cache:42 (OrderView)
    public String generateCacheKey(Long orderID) {
        return getUnderlier() + ":" + getBookType() + ":cache:" + orderID;
    }

    // Completed Orders
    // spx:bids:co (List<OrderView>)
    public String generateCompletedOrdersKey() {
        return getUnderlier() + ":" + getBookType() + ":" + "co";
    }

    public Map<String, String> orderViewToMap(OrderView order) {
        Map<String, String> map = new HashMap<>();
        map.put("id", order.getId().toString());
        map.put("userID", order.getUserID());
        map.put("symbol", order.getSymbol());
        map.put("quantity", order.getQuantity().toString());
        map.put("price", order.getPrice().toString());
        map.put("side", order.getSide());
        map.put("quantityRemaining", order.getQuantityRemaining().toString());
        return map;
    }

    @Override
    public void add(OrderView order) throws Exception {
        log.info("Starting to add order to REDIS: " + order.toString());
        long id = order.getId();
        double price = order.getPrice();
        long quantity = order.getQuantity();

        if (jedis.exists(generateCacheKey(id))) {
            throw new Exception("Order already exists. Do not use add -- use update");
        }

        if (jedis.exists(generateOrdersGroupedByPriceKey(order))) {
            throw new Exception("Order already exists. Do not use add -- use update");
        }

        jedis.hmset(generateCacheKey(id), orderViewToMap(order));
        jedis.incrBy(generateSharesPerPriceKey(price), quantity);
        jedis.hmset(generateOrdersGroupedByPriceKey(order), orderViewToMap(order));
        jedis.incrBy(generateAmountAvailableKey(), quantity);
        log.info("Finished adding order to REDIS: " + order.toString());
    }

    @Override
    public OrderView remove(long orderID) throws Exception {
        long start = System.nanoTime();
        if (!cache.containsKey(orderID)) {
            throw new Exception("Order does not exist, therefore it cannot be deleted");
        }

        OrderView order = find(orderID);

        double price = order.getPrice();
        long quantityToRemove = order.getQuantityRemaining();

        order.setQuantityRemaining(0L);

        cache.remove(orderID);
        ordersGroupedByPrice.get(price).remove(orderID);
        numberOfSharesPerPrice.put(price, numberOfSharesPerPrice.get(price) - quantityToRemove);
        amountAvailable -= quantityToRemove;

        completedOrders.add(order);

//        jedis.rpush(generateCompletedOrdersKey(), String.valueOf(orderID));

        // Clean up -- remove keys that have no values
        if (numberOfSharesPerPrice.get(price).equals(0L)) {
            numberOfSharesPerPrice.remove(price);
        }

        if (ordersGroupedByPrice.get(price).size() == 0) {
            ordersGroupedByPrice.remove(price);
        }

        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000.0;
        if (orderID > 999_999 && orderID % 100_000 == 0) {
            System.out.println("Remove from cache time: " + seconds);
        }

        return order;
    }

    /*
    TODO: Update's logic needs to change. You should not be able to update the symbol, original quantity. It should just accept orderID and quantity changed.
        These orders do not have lifecycle events attached to them, so they should not have the ability to change their key economics...
     */
    @Override
    public void update(OrderView newOrder, long quantityChanged) throws Exception {
        long start = System.nanoTime();
        OrderView cacheOrder = find(newOrder.getId());

        double cacheOrderPrice = newOrder.getPrice();

        numberOfSharesPerPrice.put(cacheOrderPrice, numberOfSharesPerPrice.get(cacheOrderPrice) - quantityChanged);
        ordersGroupedByPrice.get(cacheOrderPrice).put(cacheOrder.getId(), newOrder);
        cache.put(cacheOrder.getId(), newOrder);
        amountAvailable -= quantityChanged;
        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000.0;
        if (cacheOrder.getId() > 999_999 && cacheOrder.getId() % 100_000 == 0) {
            System.out.println("Update from cache time: " + seconds);
        }
    }

    @Override
    public OrderView find(long orderID) throws Exception {
        long start = System.nanoTime();
        if (!cache.containsKey(orderID)) {
            throw new Exception("OrderID does not exist. Please check if valid orderID");
        }

        double price = cache.get(orderID).getPrice();
        OrderView output = ordersGroupedByPrice.get(price).get(orderID);

        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000.0;
        if (orderID > 999_999 && orderID % 100_000 == 0) {
            System.out.println("Find from cache time: " + seconds);
        }

        return output;
    }

    public String listAvailablePerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : getNumberOfSharesPerPrice().keySet()) {
            long amount = getNumberOfSharesPerPrice().get(price);
            output.append("[").append(price).append(" -> ").append(amount).append("],");
        }
        return output.toString();
    }

    public String listOrdersPerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : getOrdersGroupedByPrice().keySet()) {
            int size = getOrdersGroupedByPrice().get(price).size();
            output.append("[").append(price).append(" -> ").append(size).append("], ");
        }
        return output.toString();
    }

    public String listCompletedOrders() {
        StringBuilder output = new StringBuilder();
        for (OrderView order : getCompletedOrders()) {
            output.append("\n").append(order.toString());
        }
        return output.toString();
    }

    public double getAmountAvailable() {
        return amountAvailable;
    }

    public String getUnderlier() {
        return underlier;
    }

    public void setUnderlier(String underlier) {
        this.underlier = underlier;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    public void setAmountAvailable(double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public TreeMap<Double, Long> getNumberOfSharesPerPrice() {
        return numberOfSharesPerPrice;
    }

    public void setNumberOfSharesPerPrice(TreeMap<Double, Long> numberOfSharesPerPrice) {
        this.numberOfSharesPerPrice = numberOfSharesPerPrice;
    }

    public Map<Double, Map<Long, OrderView>> getOrdersGroupedByPrice() {
        return ordersGroupedByPrice;
    }

    public void setOrdersGroupedByPrice(Map<Double, Map<Long, OrderView>> ordersGroupedByPrice) {
        this.ordersGroupedByPrice = ordersGroupedByPrice;
    }

    public Map<Long, OrderView> getCache() {
        return cache;
    }

    public void setCache(Map<Long, OrderView> cache) {
        this.cache = cache;
    }

    public List<OrderView> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<OrderView> completedOrders) {
        this.completedOrders = completedOrders;
    }

}
