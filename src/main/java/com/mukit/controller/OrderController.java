package com.mukit.controller;

import com.mukit.common.request.post.PostRequestEntity;
import com.mukit.common.response.get.GetRequestResponseBody;
import com.mukit.common.response.get.GetRequestResponseEntityForList;
import com.mukit.model.view.OrderView;
import com.mukit.service.OrderService;
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
    public ResponseEntity<?> findAll() {
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
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
}
