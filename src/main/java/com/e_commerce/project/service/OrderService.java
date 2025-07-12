package com.e_commerce.project.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.persistence.UserOrder;
import com.e_commerce.project.model.persistence.repositories.OrderRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;

@Service
public class OrderService {

    private final UserRepository userRepo;
    private final OrderRepository orderRepo;

    public OrderService(UserRepository userRepo, OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
    }

    public UserOrder submitOrder(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        UserOrder order = UserOrder.createFromCart(user.getCart());
        return orderRepo.save(order);
    }

    public List<UserOrder> getOrdersForUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return orderRepo.findByUser(user);
    }
}