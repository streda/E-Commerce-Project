package com.e_commerce.project.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.requests.ModifyCartRequest;
import com.e_commerce.project.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

	private final CartService cartService;

	public CartController(CartService cartService) {
		this.cartService = cartService;
	}

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
		Cart updated = cartService.addToCart(request);
		return ResponseEntity.ok(updated);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
		Cart updated = cartService.removeFromCart(request);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/{username}")
	public ResponseEntity<Cart> getCart(@PathVariable String username) {
		Cart cart = cartService.getCartForUser(username);
		return ResponseEntity.ok(cart);
	}
}
