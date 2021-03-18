package com.gt.enigma;

import com.gt.model.view.OrderView;

import java.util.*;

public class Engine {

    public double amountAvailable = 0;

    public TreeMap<Double, Long> availablePerPrice = new TreeMap<>(Collections.reverseOrder());;
    public Map<Double, List<OrderView>> ordersPerPrice = new HashMap<>();

    public List<OrderView> completedOrders = new ArrayList<>();

    public boolean processOrder(OrderView order) {
        addOrderToCache(order);
        adjustAvailablePerPrice(order.getPrice(), order.getQuantity());
        increaseAmountAvailable(order);
        return true;
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

    private void adjustAvailablePerPrice(double price, long amount) {
        if (availablePerPrice.containsKey(price)) {
            availablePerPrice.put(price, availablePerPrice.get(price) + amount);
        } else {
            availablePerPrice.put(price, amount);
        }
    }

    private void increaseAmountAvailable(OrderView order) {
        amountAvailable += order.getQuantity();
    }

}