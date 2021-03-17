package com.gt.service;

import com.gt.common.Converter;
import com.gt.model.data.OrderData;
import com.gt.model.view.OrderView;
import com.gt.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

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

}
