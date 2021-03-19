package com.gt.enigma;

import com.gt.model.view.OrderView;

import java.util.*;

public class Engine {

    public double amountAvailable = 0;

    public TreeMap<Double, Long> availablePerPrice = new TreeMap<>(Collections.reverseOrder());;
    public Map<Double, List<OrderView>> ordersPerPrice = new HashMap<>();

    public List<OrderView> completedOrders = new ArrayList<>();

    public String displayValuesOfEngine() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------\n");
        sb.append("Total Available          -> " + this.amountAvailable + "\n");
        sb.append("Available Per Price Size -> " + listAvailablePerPrice() + "\n");
        sb.append("Orders Per Price Size    -> " + listOrdersPerPrice() + "\n");
        sb.append("Completed Orders Size    -> " + listCompletedOrders() + "\n");
        sb.append("----------------------------------");
        return sb.toString();
    }

    private String listAvailablePerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : availablePerPrice.keySet()) {
            long amount = availablePerPrice.get(price);
            output.append(" [").append(price).append(" -> ").append(amount).append("],");
        }
        return output.toString();
    }

    private String listOrdersPerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : ordersPerPrice.keySet()) {
            int size = ordersPerPrice.get(price).size();
            output.append(" [").append(price).append(" -> ").append(size).append("],");
        }
        return output.toString();
    }

    private String listCompletedOrders() {
        StringBuilder output = new StringBuilder();
        for (OrderView order : completedOrders) {
            output.append(order.toString());
        }
        return output.toString();
    }

    public boolean processOrder(OrderView order) {
        addOrderToCache(order);
        increaseAmountAvailablePerPrice(order.getPrice(), order.getQuantity());
        increaseAmountAvailable(order);

        return true;
    }

    public boolean removeOrder(OrderView order) throws Exception {
        removeOrderFromCache(order);
        decreaseAmountAvailablePerPrice(order.getPrice(), order.getQuantity());
        decreaseTotalAmountAvailable(order);

        completedOrders.add(order);
        return true;
    }

    public String matchOrder(OrderView incomingOrder) {
        Set<Double> prices = findTheBestPricesToFillWith(incomingOrder);
        return "The prices that we are looking at -> " + Arrays.toString(prices.toArray());
    }

    public Set<Double> findTheBestPricesToFillWith(OrderView order) {
        if (amountAvailable < order.getQuantity()) {
            return new HashSet<>();
        } else {
            long toBeFilled = order.getQuantity();
            Set<Double> prices = new HashSet<>();
            for (Map.Entry<Double, Long> entry : availablePerPrice.entrySet()) {
                if (toBeFilled == 0) return prices;
                if (entry.getValue() > 0) {
                    if (toBeFilled <= entry.getValue()) {
                        toBeFilled = 0;
                    } else {
                        toBeFilled -= entry.getValue();
                    }
                    prices.add(entry.getKey());
                }
            }
            return prices;
        }
    }

    private void addOrderToCache(OrderView order) {
        if (ordersPerPrice.containsKey(order.getPrice())) {
            List<OrderView> values = ordersPerPrice.get(order.getPrice());
            values.add(order);
            ordersPerPrice.put(order.getPrice(), values);
        } else {
            ordersPerPrice.put(order.getPrice(), new ArrayList<>(Collections.singletonList(order)));
        }
    }

    private void removeOrderFromCache(OrderView order) {
        final double orderPrice = order.getPrice();
        if (ordersPerPrice.containsKey(orderPrice) && ordersPerPrice.get(orderPrice).contains(order)) {
            ordersPerPrice.get(orderPrice).remove(order);
        } else {
            System.out.println("Did not find matching order to remove at price: " + orderPrice);
        }
    }

    private void increaseAmountAvailablePerPrice(double price, long amount) {
        if (availablePerPrice.containsKey(price)) {
            availablePerPrice.put(price, availablePerPrice.get(price) + amount);
        } else {
            availablePerPrice.put(price, amount);
        }
    }

    private void decreaseAmountAvailablePerPrice(double price, long amount) throws Exception {
        if (availablePerPrice.containsKey(price)) {
            availablePerPrice.put(price, availablePerPrice.get(price) - amount);
        } else {
            throw new Exception("Cannot take away from something that does not exist!");
        }
    }

    private void increaseAmountAvailable(OrderView order) {
        amountAvailable += order.getQuantity();
    }

    private void decreaseTotalAmountAvailable(OrderView order) {
        amountAvailable -= order.getQuantity();
    }

}