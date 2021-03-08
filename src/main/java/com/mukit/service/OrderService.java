package com.mukit.service;

import com.mukit.common.Converter;
import com.mukit.model.data.Order;
import com.mukit.model.view.OrderView;
import com.mukit.repository.OrderRepository;
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
            List<Order> data = orderRepository.findAll();
            Converter.dataToViewModelConverterForList(orderViewList, data);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return orderViewList;
    }

    public boolean save(OrderView orderView) {
        Order order = new Order();
        try {
            Converter.viewToDataModelConverter(orderView, order);
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
