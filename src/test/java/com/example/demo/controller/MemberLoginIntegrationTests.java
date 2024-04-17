package com.example.demo.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class MemberLoginIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginSuccess() throws Exception {
        // John Doe's login
        mockMvc.perform(post("/login")
                        .param("phone", "9871234567")
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection()) // Assuming a successful login redirects to a home page
                .andExpect(redirectedUrl("/home_page"));

        // Emily Davis's login
        mockMvc.perform(post("/login")
                        .param("phone", "9672746283")
                        .param("password", "password456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home_page"));
    }

    @Test
    void loginFailure_WrongPassword() throws Exception {
        // Using John Doe's phone with a wrong password
        mockMvc.perform(post("/login")
                        .param("phone", "9871234567")
                        .param("password", "wrongPassword"))
                .andExpect(status().isOk()) // Assuming the page reloads on login failure
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void loginFailure_NoSuchUser() throws Exception {
        // Nonexistent phone number
        mockMvc.perform(post("/login")
                        .param("phone", "1000000000")
                        .param("password", "password123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"));
    }
}
