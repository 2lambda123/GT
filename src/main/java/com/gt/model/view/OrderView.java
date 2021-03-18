package com.gt.model.view;

public class OrderView {
    private Long id;
    private String symbol;
    private Long quantity;
    private Double price;

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

    public OrderView() {}

    public OrderView(Long id, String symbol, Long quantity, Double price) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderView [id=" + this.id + ", symbol=" + this.symbol + ", quantity=" + this.quantity + ", price=" + this.price + "]";
    }
}
