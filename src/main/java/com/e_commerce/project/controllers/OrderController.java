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
