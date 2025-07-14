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
