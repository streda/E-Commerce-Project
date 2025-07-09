package com.e_commerce.project.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.e_commerce.project.model.persistence.repositories.CartRepository;
import com.e_commerce.project.model.persistence.repositories.UserRepository;
import com.e_commerce.project.model.requests.CreateUserRequest;
import com.e_commerce.project.model.persistence.User;

// Add this import if TestUtils exists in your test utilities package
import com.e_commerce.project.TestUtils;

public class UserControllerTest {
    private UserController userController;

    private UserRepository userRepositoryMock = mock(UserRepository.class);
    private CartRepository cartControllerMock = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepositoryMock);
        TestUtils.injectObjects(userController, "cartRepository", cartControllerMock);
        TestUtils.injectObjects(userController, "userRepository", userRepositoryMock);
    }

    @Test
    public void create_user_happy_Path() throws Exception {
        when(encoder.encode("testtest")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<?> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());

        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(0, u.getUsername());
        assertEquals(0, u.getPassword());
    }
}
