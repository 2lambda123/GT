package com.gt.asgard.objects.base;

import com.gt.asgard.enums.BookType;
import com.gt.common.view.OrderView;

import java.util.*;

public abstract class Book {
    public BookType bookType;

    private double amountAvailable = 0;
    private TreeMap<Double, Long> availablePerPrice;
    private Map<Double, List<OrderView>> ordersPerPrice;
    private List<OrderView> completedOrders;

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }

    public double getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(double amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public TreeMap<Double, Long> getAvailablePerPrice() {
        return availablePerPrice;
    }

    public void setAvailablePerPrice(TreeMap<Double, Long> availablePerPrice) {
        this.availablePerPrice = availablePerPrice;
    }

    public Map<Double, List<OrderView>> getOrdersPerPrice() {
        return ordersPerPrice;
    }

    public void setOrdersPerPrice(Map<Double, List<OrderView>> ordersPerPrice) {
        this.ordersPerPrice = ordersPerPrice;
    }

    public List<OrderView> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<OrderView> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public Book(BookType bookType) {
        this.bookType = bookType;
        if (bookType == BookType.BID) {
            this.availablePerPrice = new TreeMap<>(Collections.reverseOrder());
        } else {
            this.availablePerPrice = new TreeMap<>();
        }
        this.ordersPerPrice = new HashMap<>();
        this.completedOrders = new ArrayList<>();
    }

    public String displayValueOfBook() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------\n");
        sb.append("Total Available      -> " + this.amountAvailable + "\n");
        sb.append("Available Per Price  -> " + listAvailablePerPrice() + "\n");
        sb.append("Orders Per Price     -> " + listOrdersPerPrice() + "\n");
        sb.append("Completed Orders:" + listCompletedOrders() + "\n");
        sb.append("----------------------------------");
        return sb.toString();
    }

    private String listAvailablePerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : availablePerPrice.keySet()) {
            long amount = availablePerPrice.get(price);
            output.append("[").append(price).append(" -> ").append(amount).append("],");
        }
        return output.toString();
    }

    private String listOrdersPerPrice() {
        StringBuilder output = new StringBuilder();
        for (double price : ordersPerPrice.keySet()) {
            int size = ordersPerPrice.get(price).size();
            output.append("[").append(price).append(" -> ").append(size).append("], ");
        }
        return output.toString();
    }

    private String listCompletedOrders() {
        StringBuilder output = new StringBuilder();
        for (OrderView order : completedOrders) {
            output.append("\n").append(order.toString());
        }
        return output.toString();
    }

    public boolean addOrder(OrderView order) {
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

    public void addToCompletedOrders(OrderView order) {
        this.completedOrders.add(order);
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
