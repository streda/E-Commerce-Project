package com.e_commerce.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.e_commerce.project.model.persistence.UserOrder;
import com.e_commerce.project.service.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		UserOrder order = orderService.submitOrder(username);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> history(@PathVariable String username) {
		List<UserOrder> orders = orderService.getOrdersForUser(username);
		return ResponseEntity.ok(orders);
	}
}

// package com.e_commerce.project.controllers;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.e_commerce.project.model.persistence.Cart;
// import com.e_commerce.project.model.persistence.User;
// import com.e_commerce.project.model.persistence.UserOrder;
// import com.e_commerce.project.model.persistence.repositories.CartRepository;
// import com.e_commerce.project.model.persistence.repositories.OrderRepository;
// import com.e_commerce.project.model.persistence.repositories.UserRepository;

// @RestController
// @RequestMapping("/api/order")
// public class OrderController {

// @Autowired
// private UserRepository userRepository;

// @Autowired
// private OrderRepository orderRepository;

// @PostMapping("/submit/{username}")
// public ResponseEntity<UserOrder> submit(@PathVariable String username) {
// User user = userRepository.findByUsername(username);
// if (user == null) {
// return ResponseEntity.notFound().build();
// }
// UserOrder order = UserOrder.createFromCart(user.getCart());
// orderRepository.save(order);
// return ResponseEntity.ok(order);
// }

// @GetMapping("/history/{username}")
// public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String
// username) {
// User user = userRepository.findByUsername(username);
// if (user == null) {
// return ResponseEntity.notFound().build();
// }
// return ResponseEntity.ok(orderRepository.findByUser(user));
// }
// }
