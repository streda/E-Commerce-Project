package com.e_commerce.project.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.requests.CreateUserRequest;
import com.e_commerce.project.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> byId(@PathVariable Long id) {
		return ResponseEntity.of(userService.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> byUsername(@PathVariable String username) {
		log.debug("Looking up user '{}'", username);
		return userService.findByUsername(username)
				.map(u -> {
					log.info("User '{}' found with id {}", username, u.getId());
					return ResponseEntity.ok(u);
				})
				.orElseGet(() -> {
					log.info("User '{}' not found", username);
					return ResponseEntity.notFound().build();
				});
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody CreateUserRequest request) {
		try {
			User u = userService.createUser(request);
			log.info("Splunk test log - order placed!");
			log.info("User creation successful: {}", request.getUsername());
			return ResponseEntity.ok(u);
		} catch (IllegalArgumentException iae) {
			// iae.getMessage() can be "Password must be at least 7 characters"
			log.error("User creation failed: {}", iae.getMessage());
			return ResponseEntity.badRequest().body(iae.getMessage());
		}
	}

	@GetMapping("/test-log")
	public ResponseEntity<String> testLog() {
		log.info("Splunk HEC debug log test from /api/user/test-log");
		return ResponseEntity.ok("Log sent!");
	}
}
