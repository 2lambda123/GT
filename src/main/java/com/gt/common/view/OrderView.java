package com.gt.common.view;

import java.util.Objects;

public class OrderView {
    private Long id;
    private String symbol;
    private Long quantity;
    private Double price;
    private String side;
    private Long quantityRemaining;

    public Long getId() {
        return id;
    }
    public Long getQuantity() {
        return quantity;
    }
    public String getSymbol() {
        return symbol;
    }
    public Double getPrice() { return price; }
    public String getSide() { return side; }
    public Long getQuantityRemaining() { return quantityRemaining; }

    public void setId(Long id) {
        this.id = id;
    }
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setPrice(Double price) { this.price = price; }
    public void setSide(String side) { this.side = side; }
    public void setQuantityRemaining(Long quantityRemaining) { this.quantityRemaining = quantityRemaining; }

    public OrderView() {}

    public OrderView(Long id, String symbol, Long quantity, Double price, String side) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.quantityRemaining = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderView orderView = (OrderView) o;
        return Objects.equals(id, orderView.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OrderView [id=" + this.id + ", symbol=" + this.symbol +
                ", quantity=" + this.quantity + ", price=" + this.price +
                ", side=" + this.side + ", remaining quantity=" + this.quantityRemaining + "]";
    }
}
