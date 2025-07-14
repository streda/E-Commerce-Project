package com.e_commerce.project.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.requests.CreateUserRequest;
import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final CartRepository cartRepo;
    private final BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepo,
            CartRepository cartRepo,
            BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.encoder = encoder;
    }

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userRepo.findByUsername(username));
    }

    public User createUser(CreateUserRequest req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirmPassword do not match");
        }
        if (req.getPassword().length() < 7) {
            throw new IllegalArgumentException("Password must be at least 7 characters");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));

        Cart cart = new Cart();
        cartRepo.save(cart);
        user.setCart(cart);

        return userRepo.save(user);
    }
}