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

// package com.e_commerce.project.controllers;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.when;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.http.ResponseEntity;
// import org.springframework.http.HttpStatus;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// import com.e_commerce.project.model.persistence.repositories.CartRepository;
// import com.e_commerce.project.model.persistence.repositories.UserRepository;
// import com.e_commerce.project.model.requests.CreateUserRequest;
// import com.e_commerce.project.model.persistence.Cart;
// import com.e_commerce.project.model.persistence.User;

// // My Test Utils should be implemented and Imported here.
// import com.e_commerce.project.TestUtils;

// public class UserControllerTest {
// private UserController userController;

// private UserRepository userRepositoryMock = mock(UserRepository.class);
// private CartRepository cartControllerMock = mock(CartRepository.class);
// private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

// @BeforeEach
// public void setUp() {
// userController = new UserController();
// TestUtils.injectObjects(userController, "userRepository",
// userRepositoryMock);
// TestUtils.injectObjects(userController, "cartRepository",
// cartControllerMock);
// TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

// // whenever cartRepo.save(...) is called, just return the same Cart
// when(cartControllerMock.save(any(Cart.class)))
// .thenAnswer(invocation -> invocation.getArgument(0));

// // whenever userRepo.save(...) is called,
// // set id=0, return the same User
// when(userRepositoryMock.save(any(User.class)))
// .thenAnswer(invocation -> {
// User u = invocation.getArgument(0);
// u.setId(0L);
// return u;
// });
// }

// @Test
// public void create_user_happy_Path() throws Exception {
// when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
// CreateUserRequest r = new CreateUserRequest();
// r.setUsername("test");
// r.setPassword("testPassword");
// r.setConfirmPassword("testPassword");

// final ResponseEntity<?> response = userController.createUser(r);
// assertNotNull(response);
// assertEquals(HttpStatus.OK, response.getStatusCode());

// User u = (User) response.getBody();
// assertNotNull(u);
// assertEquals(0, u.getId());
// assertEquals("test", u.getUsername());
// assertEquals("thisIsHashed", u.getPassword());
// }

// @Test
// public void createUser_passwordTooShort() {
// CreateUserRequest r = new CreateUserRequest();
// r.setUsername("u");
// r.setPassword("abc");
// r.setConfirmPassword("abc");

// ResponseEntity<?> resp = userController.createUser(r);

// assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
// assertTrue(
// resp.getBody().toString().contains("at least 7"),
// "Expected a message about minimum length");
// }
// }
