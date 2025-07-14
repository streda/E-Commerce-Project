package com.e_commerce.project.controllers;

import com.e_commerce.project.service.CartService;
import com.e_commerce.project.security.JWTUtil;
import com.e_commerce.project.security.SecurityConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        CartService cartService;

        @MockBean
        JWTUtil jwtUtil;

        private String validToken;

        @BeforeEach
        void setup() throws Exception {
                validToken = obtainJwtToken();
                Mockito.when(jwtUtil.validateToken(validToken)).thenReturn(true);
                Mockito.when(jwtUtil.getUsernameFromToken(validToken)).thenReturn("testuser");
        }

        @Test
        void addToCart_requiresAuth() throws Exception {
                mockMvc.perform(post("/api/cart/addToCart")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"testuser\",\"itemId\":1,\"quantity\":2}"))
                                .andExpect(status().isForbidden());
        }

        @Test
        void addToCart_withValidToken() throws Exception {
                mockMvc.perform(post("/api/cart/addToCart")
                                .header(SecurityConstants.HEADER_STRING,
                                                SecurityConstants.TOKEN_PREFIX + validToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\":\"testuser\",\"itemId\":1,\"quantity\":2}"))
                                .andExpect(status().isOk());
        }

        private final String username = "testuser_" + UUID.randomUUID();
        private final String password = "testpass123";

        private void createUserIfNeeded() throws Exception {
                try {
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
                } catch (Exception e) {
                        // ignore or log
                }
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
                System.out.println("CartControllerTest JWT: " + tokenHeader); // DEBUG

                return tokenHeader.replace("Bearer ", "");
        }
}
