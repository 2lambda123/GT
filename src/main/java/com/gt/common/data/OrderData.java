package com.gt.common.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "ORDERS")
public class OrderData {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "SYMBOL")
    private String symbol;

    @NotNull
    @Column(name = "QUANTITY")
    private Long quantity;

    @NotNull
    @Column(name = "PRICE")
    private Double price;

    @NotNull
    @Column(name = "SIDE")
    private String side;

    @NotNull
    @Column(name = "QUANTITY_REMAINING")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderData orderData = (OrderData) o;
        return Objects.equals(id, orderData.id) && Objects.equals(symbol, orderData.symbol)
                && Objects.equals(quantity, orderData.quantity) && Objects.equals(price, orderData.price)
                && Objects.equals(side, orderData.side) && Objects.equals(quantityRemaining, orderData.quantityRemaining);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, quantity, price, side, quantityRemaining);
    }

    public OrderData() {}

    public OrderData(@NotNull String symbol, @NotNull Long quantity, @NotNull Double price, @NotNull String side, @NotNull Long quantityRemaining) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.quantityRemaining = quantityRemaining;
    }

    public OrderData(@NotNull Long id, @NotNull String symbol, @NotNull Long quantity, @NotNull Double price, @NotNull String side, @NotNull Long quantityRemaining) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.side = side;
        this.quantityRemaining = quantityRemaining;
    }

    @Override
    public String toString() {
        return "Order [id=" + this.id + ", symbol=" + this.symbol +
                ", quantity=" + this.quantity + ", price=" + this.price +
                ", side=" + this.side + ", quantity remaining=" + this.quantityRemaining + "]";
    }

}
