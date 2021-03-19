package com.gt.controller;

import com.gt.enigma.Engine;
import com.gt.model.view.OrderView;
import com.gt.service.OrderService;
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
public class OrderController {

    @Autowired
    OrderService orderSerivce;

    Engine engine;

    public OrderController() {
        engine = new Engine();
    }

    @GetMapping("/all")
    public List<OrderView> findAll() {
        return orderSerivce.findAll();
    }

    @GetMapping("/count")
    public Integer numberOfOrdersInDatabase() {
        return orderSerivce.getNumberOfOrdersInDatabase();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@RequestBody OrderView order) {
        boolean didProcess = engine.processOrder(order);
        log.info("It processed correctly: " + didProcess);
        String matchOutput = engine.matchOrder(order);
        log.info(matchOutput);
        boolean success = orderSerivce.save(order);
        log.info("It saved in the database: " + success);
        if (didProcess && success) {
            log.info("Added order. Logging engine values: \n");
            log.info("\n" + engine.displayValuesOfEngine());
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        log.severe("Failed to add order. Logging engine values: \n");
        log.severe(engine.displayValuesOfEngine());
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
        return orderSerivce.updateOrder(id, inpuOrder);
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
