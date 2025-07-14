package com.e_commerce.project.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.e_commerce.project.model.requests.CreateUserRequest;
import com.e_commerce.project.model.persistence.User;
import com.e_commerce.project.service.UserService;

class UserControllerTest {

    private UserController userController;
    private UserService userService = mock(UserService.class);

    @BeforeEach
    void setUp() {
        // use the new constructor
        userController = new UserController(userService);
    }

    @Test
    void createUser_happyPath() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testPassword");
        req.setConfirmPassword("testPassword");

        // prepare a dummy user to return from the service
        User dummy = new User();
        dummy.setId(0L);
        dummy.setUsername("test");
        dummy.setPassword("hashed");

        when(userService.createUser(any(CreateUserRequest.class)))
                .thenReturn(dummy);

        ResponseEntity<?> resp = userController.createUser(req);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        User body = (User) resp.getBody();
        assertNotNull(body);
        assertEquals(0L, body.getId());
        assertEquals("test", body.getUsername());
        assertEquals("hashed", body.getPassword());
    }

    @Test
    void createUser_passwordMismatch() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("x");
        req.setPassword("foo");
        req.setConfirmPassword("bar");

        // tells the mock to behave like the real service
        when(userService.createUser(req))
                .thenThrow(new IllegalArgumentException("Password and confirmPassword do not match"));

        ResponseEntity<?> resp = userController.createUser(req);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertTrue(((String) resp.getBody())
                .contains("confirmPassword do not match"));
    }
}
