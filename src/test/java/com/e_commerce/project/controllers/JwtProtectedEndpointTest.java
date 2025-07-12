package com.e_commerce.project.controllers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtProtectedEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessProtectedWithoutToken_fails() throws Exception {
        mockMvc.perform(get("/api/cart/testuser"))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessProtectedWithToken_succeeds() throws Exception {
        String token = obtainJwtToken(); // simulate login if implemented
        mockMvc.perform(get("/api/cart/testuser")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    // Before creating user, try login first
    private String obtainJwtToken() throws Exception {
        String username = "testuser";
        String password = "testpass";

        // Try login first
        try {
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

            return result.getResponse().getHeader("Authorization").replace("Bearer ", "");
        } catch (AssertionError e) {
            // If login fails, proceed to create user
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

            return result.getResponse().getHeader("Authorization").replace("Bearer ", "");
        }
    }

    @Test
    void accessOrderHistoryWithToken_succeeds() throws Exception {
        String token = obtainJwtToken();

        mockMvc.perform(get("/api/order/history/testuser")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void accessOrderHistoryWithoutToken_fails() throws Exception {
        mockMvc.perform(get("/api/order/history/testuser"))
                .andExpect(status().isForbidden());
    }

}