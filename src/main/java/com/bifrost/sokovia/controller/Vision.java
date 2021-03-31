package com.bifrost.sokovia.controller;

import com.bifrost.asgard.Thor;
import com.bifrost.common.api.UserLoginRequest;
import com.bifrost.common.api.UserLoginResponse;
import com.bifrost.common.view.OrderView;
import com.bifrost.common.view.UserView;
import com.bifrost.sokovia.service.Wanda;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@CrossOrigin
@RestController
@RequestMapping("order")
public class Vision {

    @Autowired
    Wanda orderSerivce;

    Thor engine;

    public Vision() {
        engine = new Thor();
    }

//    @GetMapping("/redis")
//    public String getUser() {
//        return orderSerivce.getUser();
//    }

    @GetMapping("/all")
    public List<OrderView> findAll() {
        return orderSerivce.findAll();
    }

    @GetMapping("/all/users")
    public List<UserView> getAllUsersInDatabase() {
        return orderSerivce.getAllUsersInDatabase();
    }

    @GetMapping("/users/validate/{userID}")
    public boolean isUserInDatabase(@PathVariable String userID) {
        return orderSerivce.isUserInDatabase(userID);
    }

    @PostMapping("/validate/login")
    public UserLoginResponse isCorrectLogin(@RequestBody UserLoginRequest userLoginRequest) {
        return new UserLoginResponse(orderSerivce.isCorrectLogin(userLoginRequest.getUsername(), userLoginRequest.getPassword()));
    }

    @PostMapping("/create/user")
    public String createNewUser(@RequestBody UserLoginRequest userLoginRequest) {
        return orderSerivce.createNewUser(userLoginRequest);
    }

    @GetMapping("/get/user/{userID}")
    public List<OrderView> getAllUserOrders(@PathVariable String userID) {
        return orderSerivce.getAllOrdersForUser(userID);
    }

    @GetMapping("/count")
    public Integer numberOfOrdersInDatabase() {
        return orderSerivce.getNumberOfOrdersInDatabase();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderView order) {
//        engine.acceptOrder(order);
        log.info("Thor processed the order...");
        boolean success = orderSerivce.save(order);
        log.info("It saved in the database: " + success);
        if (success) {
            log.info("Added order. Logging engine values: \n");
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        log.severe("Failed to add order. Logging engine values: \n");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/{id}")
    public OrderView getOrder(@PathVariable Long id) {
        OrderView orderView = orderSerivce.getOrder(id);
        System.out.println(orderView);
        return orderView;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        return orderSerivce.deleteOrder(id);
    }

    @RequestMapping(value = "/put/{id}", method = RequestMethod.PUT)
    public String updateUser(@PathVariable Long id, @RequestBody OrderView inpuOrder) {
        return orderSerivce.updateOrder(inpuOrder);
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
