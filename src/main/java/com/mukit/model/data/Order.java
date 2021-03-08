package com.mukit.model.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "ORDERS")
public class Order {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return symbol.equals(order.symbol) && quantity.equals(order.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, quantity);
    }

    public Order() {}

    public Order(Long id, @NotNull String symbol, @NotNull Long quantity) {
        this.id = id;
        this.symbol = symbol;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Order [id=" + this.id + ", symbol=" + this.symbol + ", quantity=" + this.quantity + "]";
    }

}
