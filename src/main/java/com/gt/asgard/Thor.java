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

    public void acceptOrder(OrderView order) throws Exception {
        Symbol symbol = retrieveOrderSymbol(order.getSymbol());
        if (order.getSide().equals("buy")) {
            symbol.bids.addOrder(order);
            MatchOutput matchOutput = matchOrder(order, symbol.asks);
            if (matchOutput.isFullyMatch()) {
                if (debug) System.out.println("The buy order was fully matched!");
                symbol.bids.removeOrder(matchOutput.getModifiedOrder().getId());
            } else {
                if (debug) System.out.println("The buy order was partially matched!");
//                symbol.bids.updateOrder(matchOutput.getModifiedOrder());
            }
        } else {
            symbol.asks.addOrder(order);
//            OrderView incomingOrder = matchOrder(order, symbol.bids);
//            if (incomingOrder.getQuantity() > 0) {
//                symbol.asks.addOrder(incomingOrder);
//            } else {
//                if (debug) System.out.println("The sell order was fully matched!");
//            }
        }
        if (debug) {
            System.out.println("----------------------------------");
            System.out.println("     ----- " + symbol.underlier + "'s Bids -----");
            System.out.println(symbol.bids.displayValueOfBook());
            System.out.println("     ----- " + symbol.underlier + "'s Asks -----");
            System.out.println(symbol.asks.displayValueOfBook());
        }
    }

    private Symbol retrieveOrderSymbol(String symbol) {
        if (!symbols.containsKey(symbol)) {
            symbols.put(symbol, new Symbol(symbol));
        }
        return symbols.get(symbol);
    }

    private MatchOutput matchOrder(OrderView incomingOrder, Book book) throws Exception {
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

                    return new MatchOutput(true, incomingOrder);
                } else if (incomingOrder.getQuantity() < existingOrder.getQuantityRemaining()) {
                    long quantityChanging = incomingOrder.getQuantity();

                    existingOrder.setQuantityRemaining(existingOrder.getQuantityRemaining() - quantityChanging);

                    book.tesseract.update(existingOrder, quantityChanging);

                    return new MatchOutput(true, incomingOrder);
//                } else {
//                    fillExistingOrder(existingOrder, book);
//                    existingOrdersFilled.add(existingOrder);
//                    incomingOrder.setQuantity(incomingOrder.getQuantity() - existingOrder.getQuantity());
                }
            }
        }
//        for (OrderView order : existingOrdersFilled) {
//            double price = order.getPrice();
//            book.getOrdersPerPrice().get(price).remove(order);
//            if (book.getOrdersPerPrice().get(price).size() == 0) {
//                book.getOrdersPerPrice().remove(price);
//            }
//        }
        return new MatchOutput(false, incomingOrder);
    }

    private Set<Double> findTheBestPricesToFillWith(OrderView order, Book book) {
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
            return prices;
        }
    }

//    private void fillExistingOrder(OrderView order, Book book) {
//        double price = order.getPrice();
//        book.getAvailablePerPrice().put(price, (book.getAvailablePerPrice().get(price)) - order.getQuantity());
//        if (book.getAvailablePerPrice().get(price) == 0) {
//            book.getAvailablePerPrice().remove(price);
//        }
//        book.setAmountAvailable(book.getAmountAvailable() - order.getQuantity());
//        order.setQuantity(0L);
//        book.addToCompletedOrders(order);
//    }

//    private void updateExistingOrder(OrderView order, long numOfSharesFilled, Book book) {
//        double price = order.getPrice();
//        book.getAvailablePerPrice().put(price, (book.getAvailablePerPrice().get(price)) - numOfSharesFilled);
//        int index = book.getOrdersPerPrice().get(price).indexOf(order);
//        long quantity = book.getOrdersPerPrice().get(price).get(index).getQuantity();
//        book.getOrdersPerPrice().get(price).get(index).setQuantity(quantity - numOfSharesFilled);
//        book.setAmountAvailable(book.getAmountAvailable() - numOfSharesFilled);
//    }

}

class MatchOutput {
    public boolean fullyMatch;
    public OrderView modifiedOrder;

    public MatchOutput(boolean fullyMatch, OrderView modifiedOrder) {
        this.fullyMatch = fullyMatch;
        this.modifiedOrder = modifiedOrder;
    }

    public boolean isFullyMatch() {
        return fullyMatch;
    }

    public void setFullyMatch(boolean fullyMatch) {
        this.fullyMatch = fullyMatch;
    }

    public OrderView getModifiedOrder() {
        return modifiedOrder;
    }

    public void setModifiedOrder(OrderView modifiedOrder) {
        this.modifiedOrder = modifiedOrder;
    }
}