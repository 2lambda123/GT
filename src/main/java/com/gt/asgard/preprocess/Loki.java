package com.gt.enigma.preprocess;

import com.gt.model.view.OrderView;

import java.util.HashSet;
import java.util.Set;

// Pre Processor
public class Loki {

    public Set<String> allowedOrders = new HashSet<>();

    public Loki(String allowedSymbol) {
        allowedOrders.add(allowedSymbol);
    }

    public ValidationObject validate(OrderView order) {
        ValidationObject validation = new ValidationObject();
        validation = validateOrderDetails(order, validation);
        validation = validateSymbol(order, validation);
        validation.setFullyValidated(true);
        return validation;
    }

    private ValidationObject validateSymbol(OrderView order, ValidationObject validation) {
        if (!allowedOrders.contains(order.getSymbol())) {
            validation.getErrors().add("The order's symbol '" + order.getSymbol() + "' is not a symbol allowed on the exchange");
            return validation;
        }
        return validation;
    }

    private ValidationObject validateOrderDetails(OrderView order, ValidationObject validation) {
        if (order == null) {
            validation.getErrors().add("Order cannot be null");
            return validation;
        }
        if (order.getId() == null) {
            validation.getErrors().add("Order ID cannot be null");
            return validation;
        }
        if (order.getId() < 0) {
            validation.getErrors().add("Order ID cannot be less than 0");
            return validation;
        }
        if (order.getQuantity() == null) {
            validation.getErrors().add("Quantity cannot be null");
            return validation;
        }
        if (order.getQuantity() < 0) {
            validation.getErrors().add("Quantity cannot be less than 0");
            return validation;
        }
        if (order.getSymbol() == null) {
            validation.getErrors().add("Symbol cannot be null");
            return validation;
        }
        if (order.getSymbol().isEmpty()) {
            validation.getErrors().add("Symbol cannot be blank");
            return validation;
        }
        if (order.getPrice() == null) {
            validation.getErrors().add("Price cannot be null");
            return validation;
        }
        if (order.getPrice() < 0) {
            validation.getErrors().add("Price cannot be less than 0");
            return validation;
        }
        if (order.getSide() == null) {
            validation.getErrors().add("Side cannot be null");
            return validation;
        }
        if (order.getSide().isEmpty()) {
            validation.getErrors().add("Symbol cannot be blank");
            return validation;
        }
        if (!order.getSide().equals("buy") && !order.getSide().equals("sell")) {
            validation.getErrors().add("Side must be: ['buy','sell']");
            return validation;
        }
        validation.setOrderId(order.getId());
        return validation;
    }

}
