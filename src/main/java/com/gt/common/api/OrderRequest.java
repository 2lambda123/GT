package com.gt.common.api;

import java.util.Objects;

public class OrderRequest {

    private String symbol;
    private Long quantity;
    private Double price;
    private String side;

    public Long getQuantity() {
        return quantity;
    }
    public String getSymbol() {
        return symbol;
    }
    public Double getPrice() { return price; }
    public String getSide() { return side; }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setPrice(Double price) { this.price = price; }
    public void setSide(String side) { this.side = side; }

    public OrderRequest() {}

    public OrderRequest(String symbol, Long quantity, Double price, String side) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderRequest that = (OrderRequest) o;
        return Objects.equals(symbol, that.symbol) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(side, that.side);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, quantity, price, side);
    }

    @Override
    public String toString() {
        return "OrderRequest [symbol=" + this.symbol +
                ", quantity=" + this.quantity + ", price=" + this.price +
                ", side=" + this.side + "]";
    }

}
