package com.bifrost.asgard;

import com.bifrost.asgard.objects.base.Book;
import com.bifrost.asgard.objects.children.MatchOutput;
import com.bifrost.asgard.objects.children.Symbol;
import com.bifrost.common.view.OrderView;
import lombok.extern.java.Log;

import java.util.*;

@Log
public class Thor {

    boolean debug = true;

    Map<String, Symbol> symbols;

    public Thor() {
        symbols = new HashMap<>();
    }

    public List<OrderView> acceptOrder(OrderView order) throws Exception {
        List<OrderView> allAffectedOrders = new ArrayList<>();
        Symbol symbol = retrieveOrderSymbol(order.getSymbol());
        long originalQuantity = order.getQuantityRemaining();
        if (order.getSide().equals("buy")) {
            // Add
            symbol.bids.addOrder(order);

            // Match
            MatchOutput matchOutput = matchOrder(order, symbol.asks);

            // Remove/Update Incoming Order
            OrderView matchedIncomingOrder = matchOutput.getModifiedOrder();

            if (matchOutput.isFullyMatch()) {
                if (debug) System.out.println("The buy order was fully matched!");
                symbol.bids.removeOrder(matchedIncomingOrder.getId());
            } else {
                if (debug) System.out.println("The buy order was partially matched!");
                symbol.bids.updateOrder(matchedIncomingOrder, originalQuantity - order.getQuantityRemaining());
            }

            allAffectedOrders.add(matchedIncomingOrder);

            // Remove/Update Existing Orders
            for (long orderID : matchOutput.getExistingOrdersToRemove()) {
                log.info("Removing the following orderID: " + orderID);
                OrderView removedOrder = symbol.asks.tesseract.remove(orderID);
                allAffectedOrders.add(removedOrder);
            }
            for (long orderID : matchOutput.getExistingOrdersToUpdate()) {
                log.info("Getting the following orderID to update (in asks): " + orderID);
                OrderView updatedOrder = symbol.asks.tesseract.find(orderID);
                allAffectedOrders.add(updatedOrder);
            }
        } else {
            // Add
            symbol.asks.addOrder(order);

            // Match
            MatchOutput matchOutput = matchOrder(order, symbol.bids);

            // Remove/Update Incoming Order
            OrderView matchedIncomingOrder = matchOutput.getModifiedOrder();

            if (matchOutput.isFullyMatch()) {
                if (debug) System.out.println("The buy order was fully matched!");
                symbol.asks.removeOrder(matchedIncomingOrder.getId());
            } else {
                if (debug) System.out.println("The buy order was partially matched!");
                symbol.asks.updateOrder(matchedIncomingOrder, originalQuantity - order.getQuantityRemaining());
            }

            allAffectedOrders.add(matchedIncomingOrder);

            // Remove/Update Existing Orders
            for (long orderID : matchOutput.getExistingOrdersToRemove()) {
                log.info("Removing the following orderID: " + orderID);
                OrderView removedOrder = symbol.bids.tesseract.remove(orderID);
                allAffectedOrders.add(removedOrder);
            }
            for (long orderID : matchOutput.getExistingOrdersToUpdate()) {
                log.info("Getting the following orderID to update (in bids): " + orderID);
                OrderView updatedOrder = symbol.bids.tesseract.find(orderID);
                allAffectedOrders.add(updatedOrder);
            }
        }
        if (debug) {
            printCacheInfo(symbol);
        }
        return allAffectedOrders;
    }

    public OrderView cancelOrder(OrderView order) throws Exception {
        long orderID = order.getId();
        Symbol symbol = this.symbols.get(order.getSymbol());
        try {
            OrderView cancelledOrder = symbol.bids.removeOrder(orderID);
            log.info("Removed order: " + cancelledOrder.getId() + " from the bids of " + order.getSymbol());
            return cancelledOrder;
        } catch (Exception e) {
            log.info("Failed to remove order from the bids. Trying in the asks...");
        }
        try {
            OrderView cancelledOrder = symbol.asks.removeOrder(orderID);
            log.info("Removed order: " + cancelledOrder.getId() + " from the asks " + order.getSymbol());
            return cancelledOrder;
        } catch (Exception e) {
            return null;
        }
    }

    private Symbol retrieveOrderSymbol(String symbol) {
        if (!symbols.containsKey(symbol)) {
            symbols.put(symbol, new Symbol(symbol));
        }
        return symbols.get(symbol);
    }

    /*
    Update is not recognizing the orders that were already on the exchange but got changed
     */
    private MatchOutput matchOrder(OrderView incomingOrder, Book book) throws Exception {
        Set<Double> bestPricesToFillWith = findTheBestPricesToFillWith(incomingOrder, book);
        Set<Long> existingOrdersToRemove = new HashSet<>();
        Set<Long> existingOrdersToUpdate = new HashSet<>();
        for (double price : bestPricesToFillWith) {
            for (OrderView existingOrder : book.tesseract.getOrdersGroupedByPrice().get(price).values()) {
                if (incomingOrder.getQuantity() == 0) {
                    break;
                }
                if (incomingOrder.getQuantity().equals(existingOrder.getQuantityRemaining())) {
                    existingOrdersToRemove.add(existingOrder.getId());

                    return new MatchOutput(true, incomingOrder, existingOrdersToRemove, existingOrdersToUpdate);
                } else if (incomingOrder.getQuantityRemaining() < existingOrder.getQuantityRemaining()) {
                    long quantityChanging = incomingOrder.getQuantityRemaining();

                    existingOrder.setQuantityRemaining(existingOrder.getQuantityRemaining() - quantityChanging);
                    book.tesseract.update(existingOrder, quantityChanging);
                    existingOrdersToUpdate.add(existingOrder.getId());

                    return new MatchOutput(true, incomingOrder, existingOrdersToRemove, existingOrdersToUpdate);
                } else {
                    long quantityChanging = existingOrder.getQuantityRemaining();

                    existingOrdersToRemove.add(existingOrder.getId());
                    incomingOrder.setQuantityRemaining(incomingOrder.getQuantityRemaining() - quantityChanging);
                }
            }
        }
        return new MatchOutput(false, incomingOrder, existingOrdersToRemove, existingOrdersToUpdate);
    }

    private Set<Double> findTheBestPricesToFillWith(OrderView order, Book book) {
        long start = System.nanoTime();
        if (book.tesseract.getAmountAvailable() < order.getQuantity()) {
            return new HashSet<>();
        } else {
            long toBeFilled = order.getQuantity();
            Set<Double> prices = new HashSet<>();
            for (Map.Entry<Double, Long> entry : book.tesseract.getNumberOfSharesPerPrice().entrySet()) {
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
            long end = System.nanoTime();
            double seconds = (double) (end - start) / 1_000_000_000.0;
            if (order.getId() > 999_999 && order.getId() % 100_000 == 0) {
                System.out.println("Finding best prices to fill with time: " + seconds);
            }
            return prices;
        }
    }

    private void printCacheInfo(Symbol symbol) {
        System.out.println("----------------------------------");
        System.out.println("     ----- " + symbol.underlier + "'s Bids -----");
        System.out.println(symbol.bids.displayValueOfBook());
        System.out.println("     ----- " + symbol.underlier + "'s Asks -----");
        System.out.println(symbol.asks.displayValueOfBook());
    }
}

