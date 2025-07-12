package com.e_commerce.project.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import com.e_commerce.project.model.requests.CreateUserRequest;
import com.e_commerce.project.model.persistence.Cart;
import com.e_commerce.project.model.persistence.User;

// My Test Utils should be implemented and Imported here.
import com.e_commerce.project.TestUtils;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepositoryMock = mock(UserRepository.class);
    private CartRepository cartControllerMock = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository",
                userRepositoryMock);
        TestUtils.injectObjects(userController, "cartRepository",
                cartControllerMock);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        // whenever cartRepo.save(...) is called, just return the same Cart
        when(cartControllerMock.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // whenever userRepo.save(...) is called,
        // set id=0, return the same User
        when(userRepositoryMock.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(0L);
                    return u;
                });
    }

    @Test
    public void create_user_happy_Path() throws Exception {
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<?> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());
    }
}
