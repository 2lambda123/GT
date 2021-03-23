package com.gt.asgard.objects.base;

import com.gt.asgard.cache.Tesseract;
import com.gt.asgard.enums.BookType;
import com.gt.common.view.OrderView;

public abstract class Book {
    public BookType bookType;

    public Tesseract tesseract;

    public Book(BookType bookType, Tesseract cache) {
        this.bookType = bookType;
        this.tesseract = cache;
    }

    public String displayValueOfBook() {
        return "----------------------------------\n" +
                "Total Available      -> " + this.tesseract.getAmountAvailable() + "\n" +
                "Available Per Price  -> " + this.tesseract.listAvailablePerPrice() + "\n" +
                "Orders Per Price     -> " + this.tesseract.listOrdersPerPrice() + "\n" +
                "Completed Orders:" + this.tesseract.listCompletedOrders() + "\n" +
                "----------------------------------";
    }

    public void addOrder(OrderView order) throws Exception {
        this.tesseract.add(order);
    }

    public boolean removeOrder(long orderID) throws Exception {
        this.tesseract.remove(orderID);

        return true;
    }

    public void updateOrder(OrderView order, long quantityToChange) throws Exception {
        this.tesseract.update(order, quantityToChange);
    }

    public OrderView findOrder(long orderID) throws Exception {
        return this.tesseract.find(orderID);
    }

    public BookType getBookType() {
        return bookType;
    }

}
