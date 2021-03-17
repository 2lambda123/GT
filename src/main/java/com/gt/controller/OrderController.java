package com.gt.controller;

import com.gt.common.response.get.GetRequestResponseBody;
import com.gt.common.response.get.GetRequestResponseEntityForList;
import com.gt.model.view.OrderView;
import com.gt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    OrderService orderSerivce;

    @GetMapping("/all")
    public List<OrderView> findAll() {
        GetRequestResponseEntityForList<OrderView, Long> responseObject = new GetRequestResponseEntityForList<OrderView, Long>();
        List<OrderView> orderViewList = orderSerivce.findAll();

        if (orderViewList != null) {
            List<GetRequestResponseBody<OrderView, Long>> responseBody = new ArrayList<GetRequestResponseBody<OrderView, Long>>();
            for (OrderView orderView : orderViewList) {
                GetRequestResponseBody<OrderView, Long> getRequestResponseBody = new GetRequestResponseBody<OrderView, Long>();
                getRequestResponseBody.id = orderView.getId();
                getRequestResponseBody.type = "order";
                getRequestResponseBody.attributes = orderView;
                responseBody.add(getRequestResponseBody);
            }
            responseObject.data = responseBody;
            return orderViewList;
        } else {
            return new ArrayList<>();
        }
    }

    @GetMapping("/count")
    public Integer numberOfOrdersInDatabase() {
        return orderSerivce.getNumberOfOrdersInDatabase();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderView postRequestEntity) {
        boolean success = orderSerivce.save(postRequestEntity);
        if (success) {
            System.out.println("SUCCESS!!!");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        System.out.println("Failed!!!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderSerivce.deleteOrder(id);
    }

    @PostMapping("/match")
    public String matchOrAddOrder(@RequestBody OrderView inputOrder) {
        List<OrderView> ordersFromDatabase = findAll();
        if (ordersFromDatabase.size() > 0) {
            for (OrderView order : ordersFromDatabase) {
                if (order.getSymbol().equals(inputOrder.getSymbol()) && order.getQuantity().equals(inputOrder.getQuantity())) {
                    orderSerivce.deleteOrder(order.getId());
                    return "Found a matching order with same symbol and quantity. Removing existing order from database";
                }
            }
            orderSerivce.save(inputOrder);
            return "No orders matched -- adding order into database";
        } else {
            orderSerivce.save(inputOrder);
            return "Added in order since they were no orders to match";
        }
    }
}
