package com.gt.asgard;

import com.gt.asgard.objects.base.Book;
import com.gt.asgard.objects.children.Symbol;
import com.gt.common.view.OrderView;

import java.util.*;

public class Thor {

    boolean debug = true;

    Map<String, Symbol> symbols;

    public Thor() {
        symbols = new HashMap<>();
    }

    private void processSymbol(String symbol) {
        if (!symbols.containsKey(symbol)) {
            symbols.put(symbol, new Symbol(symbol));
        }
    }

    public void acceptOrder(OrderView order) {
        processSymbol(order.getSymbol());
        Symbol symbol = symbols.get(order.getSymbol());
        if (order.getSide().equals("buy")) {
            OrderView incomingOrder = matchOrder(order, symbol.asks);
            if (incomingOrder.getQuantity() > 0) {
                symbol.bids.addOrder(incomingOrder);
            } else {
                if (debug) System.out.println("The buy order was fully matched!");
            }
        } else {
            OrderView incomingOrder = matchOrder(order, symbol.bids);
            if (incomingOrder.getQuantity() > 0) {
                symbol.asks.addOrder(incomingOrder);
            } else {
                if (debug) System.out.println("The sell order was fully matched!");
            }
        }
        if (debug) {
            System.out.println("----------------------------------");
            System.out.println("     ----- " + symbol.underlier + "'s Bids -----");
            System.out.println(symbol.bids.displayValueOfBook());
            System.out.println("     ----- " + symbol.underlier + "'s Asks -----");
            System.out.println(symbol.asks.displayValueOfBook());
        }
    }

    private OrderView matchOrder(OrderView incomingOrder, Book book) {
        Set<Double> bestPricesToFillWith = findTheBestPricesToFillWith(incomingOrder, book);
        if (debug) System.out.println("The prices that we are looking at -> " + Arrays.toString(bestPricesToFillWith.toArray()));
        List<OrderView> existingOrdersFilled = new ArrayList<>();
        for (double price : bestPricesToFillWith) {
            if (!book.getOrdersPerPrice().containsKey(price)) continue;
            if (book.getOrdersPerPrice().get(price).size() == 0L) continue;
            for (OrderView existingOrder : book.getOrdersPerPrice().get(price)) {
                if (debug) System.out.println("Looking at:\n" + existingOrder.toString());
                if (incomingOrder.getQuantity() == 0) {
                    if (debug) System.out.println("Incoming order quantity is 0");
                    break;
                }
                if (incomingOrder.getQuantity().equals(existingOrder.getQuantity())) {
                    fillExistingOrder(existingOrder, book);
                    existingOrdersFilled.add(existingOrder);
                    book.getCompletedOrders().add(incomingOrder);
                    incomingOrder.setQuantity(0L);
//                } else if (incomingOrder.getQuantity() < existingOrder.getQuantity()) {
//                     Incoming order is gone, but there is still some left on existing order
//                     existingOrder.setQuantity(existingOrder.getQuantity() - incomingOrder.getQuantity());
//                } else {
//                     Incoming order is still there, but existing order is gone
                }
            }
        }
        for (OrderView order : existingOrdersFilled) {
            double price = order.getPrice();
            book.getOrdersPerPrice().get(price).remove(order);
            if (book.getOrdersPerPrice().get(price).size() == 0) {
                book.getOrdersPerPrice().remove(price);
            }
        }
        return incomingOrder;
    }

    private void fillExistingOrder(OrderView order, Book book) {
        double price = order.getPrice();
        book.getAvailablePerPrice().put(price, (book.getAvailablePerPrice().get(price)) - order.getQuantity());
        if (book.getAvailablePerPrice().get(price) == 0) {
            book.getAvailablePerPrice().remove(price);
        }
        book.setAmountAvailable(book.getAmountAvailable() - order.getQuantity());
        order.setQuantity(0L);
        book.addToCompletedOrders(order);
    }

    private void updateExistingOrder(OrderView order, long numOfSharesFilled, Book book) {
        double price = order.getPrice();
        book.getAvailablePerPrice().put(price, (book.getAvailablePerPrice().get(price)) - numOfSharesFilled);
        book.setAmountAvailable(book.getAmountAvailable() - numOfSharesFilled);
    }

    private Set<Double> findTheBestPricesToFillWith(OrderView order, Book book) {
        if (book.getAmountAvailable() < order.getQuantity()) {
            return new HashSet<>();
        } else {
            long toBeFilled = order.getQuantity();
            Set<Double> prices = new HashSet<>();
            for (Map.Entry<Double, Long> entry : book.getAvailablePerPrice().entrySet()) {
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

}