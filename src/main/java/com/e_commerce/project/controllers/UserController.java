package com.e_commerce.project.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import com.e_commerce.project.model.requests.CreateUserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {

		// Checking passwords match
		if (!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			return ResponseEntity
					.badRequest()
					.body("Password and confirmPassword do not match");
		}

		// Enforcing minimum length
		if (createUserRequest.getPassword().length() < 7) {
			return ResponseEntity
					.badRequest()
					.body("Password must be at least 7 characters");
		}

		// Build and save user
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		// Hash and set the password
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));

		// Create an empty cart and link
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		// Save the user (now has non-null password)
		userRepository.save(user);

		// Hide the password on the response
		user.setPassword(null);
		return ResponseEntity.ok(user);
	}

}
