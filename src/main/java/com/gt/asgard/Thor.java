package com.gt.asgard;

import com.gt.asgard.objects.base.Book;
import com.gt.asgard.objects.children.MatchOutput;
import com.gt.asgard.objects.children.Symbol;
import com.gt.common.view.OrderView;

import java.util.*;

public class Thor {

    boolean debug = false;

    Map<String, Symbol> symbols;

    public Thor() {
        symbols = new HashMap<>();
    }

    public OrderView acceptOrder(OrderView order) throws Exception {
        long start = System.nanoTime();
        Symbol symbol = retrieveOrderSymbol(order.getSymbol());
        long originalQuantity = order.getQuantityRemaining();
        if (order.getSide().equals("buy")) {
            symbol.bids.addOrder(order);
            start = System.nanoTime();
            MatchOutput matchOutput = matchOrder(order, symbol.asks);
            if (matchOutput.isFullyMatch()) {
                if (debug) System.out.println("The buy order was fully matched!");
                symbol.bids.removeOrder(matchOutput.getModifiedOrder().getId());
            } else {
                if (debug) System.out.println("The buy order was partially matched!");
                symbol.bids.updateOrder(matchOutput.getModifiedOrder(), originalQuantity - order.getQuantityRemaining());
            }
        } else {
            symbol.asks.addOrder(order);
            MatchOutput matchOutput = matchOrder(order, symbol.bids);
            if (matchOutput.isFullyMatch()) {
                if (debug) System.out.println("The buy order was fully matched!");
                symbol.asks.removeOrder(matchOutput.getModifiedOrder().getId());
            } else {
                if (debug) System.out.println("The buy order was partially matched!");
                symbol.asks.updateOrder(matchOutput.getModifiedOrder(), originalQuantity - order.getQuantityRemaining());
            }
        }
        if (debug) {
            System.out.println("----------------------------------");
            System.out.println("     ----- " + symbol.underlier + "'s Bids -----");
            System.out.println(symbol.bids.displayValueOfBook());
            System.out.println("     ----- " + symbol.underlier + "'s Asks -----");
            System.out.println(symbol.asks.displayValueOfBook());
            long end = System.nanoTime();
            System.out.println((double) (end - start) / 1_000_000_000.0);
        }
        return order;
    }

    /*
    TODO: Implement cancel
     */
    public void cancelOrder(long orderID) {}

    private Symbol retrieveOrderSymbol(String symbol) {
        if (!symbols.containsKey(symbol)) {
            symbols.put(symbol, new Symbol(symbol));
        }
        return symbols.get(symbol);
    }

    private MatchOutput matchOrder(OrderView incomingOrder, Book book) throws Exception {
        long start = System.nanoTime();
        Set<Double> bestPricesToFillWith = findTheBestPricesToFillWith(incomingOrder, book);
        if (debug) System.out.println("The prices that we are looking at -> " + Arrays.toString(bestPricesToFillWith.toArray()));
        for (double price : bestPricesToFillWith) {
            for (OrderView existingOrder : book.tesseract.getOrdersGroupedByPrice().get(price).values()) {
                if (debug) System.out.println("Looking at:\n" + existingOrder.toString());
                if (incomingOrder.getQuantity() == 0) {
                    if (debug) System.out.println("Incoming order quantity is 0");
                    break;
                }
                if (incomingOrder.getQuantity().equals(existingOrder.getQuantityRemaining())) {
                    book.tesseract.remove(existingOrder.getId());

                    long end = System.nanoTime();
                    double seconds = (double) (end - start) / 1_000_000_000.0;
                    if (incomingOrder.getId() > 999_999 && incomingOrder.getId() % 100_000 == 0) {
                        System.out.println("Matching time (start): " + seconds);
                    }
                    return new MatchOutput(true, incomingOrder);
                } else if (incomingOrder.getQuantityRemaining() < existingOrder.getQuantityRemaining()) {
                    long quantityChanging = incomingOrder.getQuantityRemaining();

                    existingOrder.setQuantityRemaining(existingOrder.getQuantityRemaining() - quantityChanging);

                    book.tesseract.update(existingOrder, quantityChanging);

                    long end = System.nanoTime();
                    double seconds = (double) (end - start) / 1_000_000_000.0;
                    if (incomingOrder.getId() > 999_999 && incomingOrder.getId() % 100_000 == 0) {
                        System.out.println("Matching time (middle): " + seconds);
                    }
                    return new MatchOutput(true, incomingOrder);
                } else {
                    long quantityChanging = existingOrder.getQuantityRemaining();

                    book.tesseract.remove(existingOrder.getId());

                    incomingOrder.setQuantityRemaining(incomingOrder.getQuantityRemaining() - quantityChanging);
                }
            }
        }
        long end = System.nanoTime();
        double seconds = (double) (end - start) / 1_000_000_000.0;
        if (incomingOrder.getId() > 999_999 && incomingOrder.getId() % 100_000 == 0) {
            System.out.println("Matching time (end): " + seconds);
        }
        return new MatchOutput(false, incomingOrder);
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
}

