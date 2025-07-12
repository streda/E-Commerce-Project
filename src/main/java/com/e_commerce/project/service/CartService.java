package com.e_commerce.project.service;

import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.Item;
import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.ItemRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import com.e_commerce.project.model.requests.ModifyCartRequest;

@Service
public class CartService {

    private final UserRepository userRepo;
    private final ItemRepository itemRepo;
    private final CartRepository cartRepo;

    public CartService(UserRepository userRepo,
            ItemRepository itemRepo,
            CartRepository cartRepo) {
        this.userRepo = userRepo;
        this.itemRepo = itemRepo;
        this.cartRepo = cartRepo;
    }

    public Cart addToCart(ModifyCartRequest req) {
        User user = userRepo.findByUsername(req.getUsername());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        Cart cart = user.getCart();
        IntStream.range(0, req.getQuantity()).forEach(i -> cart.addItem(item));
        return cartRepo.save(cart);
    }

    public Cart removeFromCart(ModifyCartRequest req) {
        User user = userRepo.findByUsername(req.getUsername());
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        Item item = itemRepo.findById(req.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        Cart cart = user.getCart();
        IntStream.range(0, req.getQuantity()).forEach(i -> cart.removeItem(item));
        return cartRepo.save(cart);
    }

    public Cart getCartForUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return user.getCart();
    }
}