package com.bifrost.sokovia.service;

import com.bifrost.common.Converter;
import com.bifrost.common.api.OrderRequest;
import com.bifrost.common.api.UserLoginRequest;
import com.bifrost.common.data.OrderData;
import com.bifrost.common.data.UserData;
import com.bifrost.common.view.OrderView;
import com.bifrost.common.view.UserView;
import com.bifrost.sokovia.repository.Pietro;
import com.bifrost.sokovia.repository.UsersRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log
@Service
public class Wanda {

    @Autowired
    private Pietro orderRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Wanda() {}

    public List<OrderView> findAll() {
        List<OrderView> orderViewList = new ArrayList<>();
        try {
            List<OrderData> data = orderRepository.findAll();
            Converter.dataToViewModelConverterForOrderList(orderViewList, data);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return orderViewList;
    }

    public List<OrderView> getAllOrdersForUser(String userID) {
        List<OrderView> orderViewList = new ArrayList<>();
        try {
            List<OrderData> listOfOrdersFromDatabase = orderRepository.findAll();
            List<OrderData> userOrders = listOfOrdersFromDatabase.stream()
                    .filter(x -> x.getUserID().equals(userID))
                    .collect(Collectors.toList());
            Converter.dataToViewModelConverterForOrderList(orderViewList, userOrders);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return orderViewList;
    }

    public List<UserView> getAllUsersInDatabase() {
        List<UserView> usersList = new ArrayList<>();
        try {
            List<UserData> data = usersRepository.findAll();
            Converter.dataToViewModelConverterForUserList(usersList, data);
        } catch (Exception e) {
            System.out.println("Something went wrong!");
        }
        return usersList;
    }

    public boolean isCorrectLogin(String username, String password) {
        List<UserView> usersList = getAllUsersInDatabase();
        for (UserView user : usersList) {
            log.info("Comparing " + user.getUserID() + ", " + user.getPassword() + " to: " + username + " and " + password);
            if (user.getUserID().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserInDatabase(String userID) {
        List<UserView> usersList = getAllUsersInDatabase();
        for (UserView user : usersList) {
            log.info("Comparing " + user.getUserID() + ", " + user.getPassword() + " to: " + userID);
            if (user.getUserID().equals(userID)) {
                return true;
            }
        }
        return false;
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

    public String createNewUser(UserLoginRequest request) {
        UserData user = new UserData();
        try {
            Converter.requestToDataConverter(request, user);
            UserData createdOrder = usersRepository.save(user);
            return createdOrder.getUserID();
        } catch (Exception e) {
            return "Adding new user failed";
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
