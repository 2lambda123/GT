package com.gt.sokovia.service;

import com.gt.common.Converter;
import com.gt.common.api.OrderRequest;
import com.gt.common.data.OrderData;
import com.gt.common.view.OrderView;
import com.gt.sokovia.repository.Pietro;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class Wanda {

    @Autowired
    private Pietro orderRepository;

    public Wanda() {}

    public List<OrderView> findAll() {
        List<OrderView> orderViewList = new ArrayList<>();
        try {
            List<OrderData> data = orderRepository.findAll();
            Converter.dataToViewModelConverterForList(orderViewList, data);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return orderViewList;
    }

    public boolean save(OrderView orderView) {
        OrderData order = new OrderData();
        try {
            Converter.viewToDataModelConverter(orderView, order);
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long createNewOrder(OrderRequest request) {
        OrderData order = new OrderData();
        try {
            Converter.requestToDataConverter(request, order);
            OrderData createdOrder = orderRepository.save(order);
            return createdOrder.getId();
        } catch (Exception e) {
            return -1;
        }
    }

    public Integer getNumberOfOrdersInDatabase() {
        Integer numberOfOrdersInDatabase = null;
        try {
            numberOfOrdersInDatabase = orderRepository.getNumberOfOrdersInDatabase();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            return null;
        }
        return numberOfOrdersInDatabase;
    }

    public String deleteOrder(Long orderId) {
        try {
            orderRepository.deleteById(orderId);
            return "Deleted!";
        } catch (Exception e) {
            return "Something went wrong deleting the order!";
        }
    }

    public OrderView getOrder(Long id) {
        OrderView orderFromDatabase = new OrderView();
        try {
            OrderData data = orderRepository.getOne(id);
            Converter.dataToViewModelConverterForSingleOrder(orderFromDatabase, data);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return orderFromDatabase;
    }

    public String updateOrder(OrderView orderView) {
        try {
            log.info("Searching for this orderID in the database: " + orderView.getId());
            OrderData data = orderRepository.getOne(orderView.getId());
            log.info("Database data: " + data.toString());
            data.setQuantity(orderView.getQuantity());
            data.setSymbol(orderView.getSymbol());
            data.setPrice(orderView.getPrice());
            data.setQuantityRemaining(orderView.getQuantityRemaining());
            log.info("Updated order data: " + data.toString());
            orderRepository.save(data);
            return "Updated order!";
        } catch (Exception e) {
            return "Something went wrong!";
        }
    }

}
