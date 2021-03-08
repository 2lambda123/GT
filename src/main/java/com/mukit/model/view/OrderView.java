package com.mukit.model.view;

public class OrderView {
    private Long id;
    private String symbol;
    private Long quantity;

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OrderView() {}

    public OrderView(Long id, String symbol, Long quantity) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderView [id=" + this.id + ", symbol=" + this.symbol + ", quantity=" + this.quantity + "]";
    }
}
