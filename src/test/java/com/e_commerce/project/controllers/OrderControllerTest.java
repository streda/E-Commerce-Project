package com.e_commerce.project.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String username;
    private final String password = "testpass123";

    private void createUserIfNeeded() throws Exception {
        username = "testuser_" + UUID.randomUUID();

        mockMvc.perform(post("/api/user/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "%s",
                          "password": "%s",
                          "confirmPassword": "%s"
                        }
                        """.formatted(username, password, password)))
                .andExpect(status().isOk());
    }

    private String obtainJwtToken() throws Exception {
        createUserIfNeeded();

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "username": "%s",
                          "password": "%s"
                        }
                        """.formatted(username, password)))
                .andExpect(status().isOk())
                .andReturn();

        String tokenHeader = result.getResponse().getHeader("Authorization");
        return tokenHeader.replace("Bearer ", "");
    }

    @Test
    public void getOrderHistory_withToken_succeeds() throws Exception {
        String token = obtainJwtToken();
        mockMvc.perform(get("/api/order/history/" + username)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void submitOrder_withToken_succeeds() throws Exception {
        String token = obtainJwtToken();
        mockMvc.perform(post("/api/order/submit/" + username)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}

// package com.e_commerce.project.controllers;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;
// import
// org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.MvcResult;
// import org.springframework.http.MediaType;

// import static
// org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static
// org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class OrderControllerTest {

// @Autowired
// private MockMvc mockMvc;

// private String obtainJwtToken() throws Exception {
// String username = "orderuser";
// String password = "testpass";

// // Create the user
// mockMvc.perform(post("/api/user/create")
// .contentType(MediaType.APPLICATION_JSON)
// .content("""
// {
// "username": "%s",
// "password": "%s",
// "confirmPassword": "%s"
// }
// """.formatted(username, password, password)))
// .andExpect(status().isOk());

// // Login to get token
// MvcResult result = mockMvc.perform(post("/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content("""
// {
// "username": "%s",
// "password": "%s"
// }
// """.formatted(username, password)))
// .andExpect(status().isOk())
// .andReturn();

// return result.getResponse().getHeader("Authorization").replace("Bearer ",
// "");
// }

// @Test
// void getOrderHistory_withToken_succeeds() throws Exception {
// String token = obtainJwtToken();

// mockMvc.perform(get("/api/order/history/orderuser")
// .header("Authorization", "Bearer " + token))
// .andExpect(status().isOk());
// }

// @Test
// void getOrderHistory_withoutToken_fails() throws Exception {
// mockMvc.perform(get("/api/order/history/orderuser"))
// .andExpect(status().isForbidden());
// }

// @Test
// void submitOrder_withToken_succeeds() throws Exception {
// String username = "orderuser2";
// String password = "testpass";

// // Create user
// mockMvc.perform(post("/api/user/create")
// .contentType(MediaType.APPLICATION_JSON)
// .content("""
// {
// "username": "%s",
// "password": "%s",
// "confirmPassword": "%s"
// }
// """.formatted(username, password, password)))
// .andExpect(status().isOk());

// // Login to get token
// MvcResult result = mockMvc.perform(post("/login")
// .contentType(MediaType.APPLICATION_JSON)
// .content("""
// {
// "username": "%s",
// "password": "%s"
// }
// """.formatted(username, password)))
// .andExpect(status().isOk())
// .andReturn();

// String token =
// result.getResponse().getHeader("Authorization").replace("Bearer ", "");

// // Add item to cart (assumes item id 1 exists in DB)
// mockMvc.perform(post("/api/cart/addToCart")
// .header("Authorization", "Bearer " + token)
// .contentType(MediaType.APPLICATION_JSON)
// .content("""
// {
// "username": "%s",
// "itemId": 1,
// "quantity": 1
// }
// """.formatted(username)))
// .andExpect(status().isOk());

// // Submit order
// mockMvc.perform(post("/api/order/submit/" + username)
// .header("Authorization", "Bearer " + token))
// .andExpect(status().isOk());
// }

// // @Test
// // void submitOrder_withoutToken_fails() throws Exception {
// // mockMvc.perform(post("/api/order/submit/someuser"))
// // .andExpect(status().isForbidden());
// // }
// }