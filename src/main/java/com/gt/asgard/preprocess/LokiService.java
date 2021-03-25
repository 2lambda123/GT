package com.gt.asgard.preprocess;

import com.gt.asgard.Thor;
import com.gt.asgard.objects.children.ValidationObject;
import com.gt.asgard.postprocess.HeimdallService;
import com.gt.common.api.OrderRequest;
import com.gt.common.view.OrderView;
import com.gt.sokovia.service.Wanda;
import lombok.extern.java.Log;
import org.hibernate.criterion.Order;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@Log
public class LokiService {

    private final Wanda database;
    private final Thor engine;
    private final HeimdallService postProcessor;

    public LokiService(Wanda database, Thor engine, HeimdallService postProcessor) {
        this.database = database;
        this.engine = engine;
        this.postProcessor = postProcessor;
    }

    /*
    TODO: Right now, it is only updating the incoming request. We have to return the list of OrderViews
    that were affected so we can update them all in the database

    i.e. -> engine.acceptOrder() should return List<OrderView>, not just one single OrderView
     */
    public OrderView submitRequest(OrderRequest request) throws Exception {
        // Validate
        ValidationObject validation = validateRequest(request);
        if (validation.containsErrors()) {
            log.severe("Validation errors in the new order request");
        }
        long orderID = database.createNewOrder(request);
        OrderView order = new OrderView(orderID, request.getSymbol(), request.getQuantity(), request.getPrice(), request.getSide());
        log.info("Created Order: " + order.toString());

        // Match
        List<OrderView> ordersToUpdate = engine.acceptOrder(order);

        // Update
        for (OrderView orderToUpdate : ordersToUpdate) {
            log.info("Affected order: " + orderToUpdate.toString());
            postProcessor.updateOrder(orderToUpdate);
        }

        return order;
    }

    public void cancelOrder(long orderID) throws Exception {
        OrderView order = database.getOrder(orderID);
        OrderView cancelledOrder = engine.cancelOrder(order);
        if (cancelledOrder == null) {
            throw new Exception("Could not cancel order on engine -- therefore, did not cancel in database");
        } else {
            database.updateOrder(cancelledOrder);
        }
    }

    private ValidationObject validateRequest(OrderRequest request) {
        ValidationObject validation = new ValidationObject();
        validation = validateOrderDetails(request, validation);
        validation = validateSymbol(request, validation);
        validation.setFullyValidated(true);
        return validation;
    }

    private ValidationObject validateSymbol(OrderRequest request, ValidationObject validation) {
        if (!request.getSymbol().equals("spx")) {
            validation.getErrors().add("The request's symbol '" + request.getSymbol() + "' is not a symbol allowed on the exchange");
            return validation;
        }
        return validation;
    }

    private ValidationObject validateOrderDetails(OrderRequest request, ValidationObject validation) {
        if (request == null) {
            validation.getErrors().add("Request cannot be null");
            return validation;
        }
        if (request.getQuantity() == null) {
            validation.getErrors().add("Quantity cannot be null");
            return validation;
        }
        if (request.getQuantity() < 0) {
            validation.getErrors().add("Quantity cannot be less than 0");
            return validation;
        }
        if (request.getSymbol() == null) {
            validation.getErrors().add("Symbol cannot be null");
            return validation;
        }
        if (request.getSymbol().isEmpty()) {
            validation.getErrors().add("Symbol cannot be blank");
            return validation;
        }
        if (request.getPrice() == null) {
            validation.getErrors().add("Price cannot be null");
            return validation;
        }
        if (request.getPrice() < 0) {
            validation.getErrors().add("Price cannot be less than 0");
            return validation;
        }
        if (request.getSide() == null) {
            validation.getErrors().add("Side cannot be null");
            return validation;
        }
        if (request.getSide().isEmpty()) {
            validation.getErrors().add("Symbol cannot be blank");
            return validation;
        }
        if (!request.getSide().equals("buy") && !request.getSide().equals("sell")) {
            validation.getErrors().add("Side must be: ['buy','sell']");
            return validation;
        }
        return validation;
    }

}
