package com.bifrost.asgard.docs;

public class EngineDOC {
/**
    /*
    - Why -
    When an order comes in, we want to know if we have enough shares to fill that order.
    If not, quickly deny it (since we aren't doing partial order yet)

    - Example -
    For example, if there are only 300 shares to be bought, and an order asks for 500,
    there is no possible way we can fulfill that order. So just deny the order

    public double amountAvailable;

    /*
    - Why -
    When an order comes in, we want to know what the best available price is. And if the order requires
    more shares than the best available price has to offer, make sure we go to the next best price

    - Example -


    public TreeMap<Double, Long> availablePerPrice;

    /*
    - Why -
    When we are matching orders, we need to be able to go through the list of orders and know which order
    to actually fill first. This is because we are matching orders based on price and time.

    - Example -


    public Map<Double, List<OrderView>> ordersPerPrice;
    public List<OrderView> completedOrders;

    public void processOrder(OrderView order) {
        addOrder(order);
        increaseAmountAvailable(order);
        updateBestPrice(order);
    }

    private void addOrder(OrderView order) {
        adjustAvailablePerPrice(order.price, order.quantity);
        if (ordersPerPrice.containsKey(order.price)) {
            List<OrderView> values = ordersPerPrice.get(order.price);
            values.add(order);
            ordersPerPrice.put(order.price, values);
        } else {
            ordersPerPrice.put(order.price, new ArrayList<>(Collections.singletonList(order)));
        }
    }

    private void increaseAmountAvailable(OrderView order) {
        amountAvailable += order.quantity;
    }

    private void updateBestPrice(Order order) {
        if (bookType == BookType.BID) {
            if (order.price > bestPrice) bestPrice = order.price;
        } else {
            if (order.price < bestPrice) bestPrice = order.price;
        }
        if (bestPrice == 0)  bestPrice = startingBestPrice;
    }

 */

}