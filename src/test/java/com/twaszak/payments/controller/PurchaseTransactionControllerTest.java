package com.twaszak.payments.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
class PurchaseTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void submit_payment_returns200andPaymentResponse() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                .content("{\"description\": \"payment for stuff fdafsdfasasssssssssssss\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("payment for stuff fdafsdfasasssssssssssss"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-09-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmount").value(9303.33))
                .andExpect(jsonPath("id").isNotEmpty());

    }

    @Test
    void submit_payment_returns400_when_description_over_fifty_chars() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"testtesttesttesttesttesttesttesttesttesttesttesttes\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("description").value("Description must not exceed 50 characters"));

    }
    @Test
    void submit_payment_returns400_when_description_empty() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("description").value("Description for purchase cannot be empty"));

    }

    @Test
    void submit_payment_returns400_when_date_invalid() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-09-30\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("message").value("Date must be in UTC and follow the format: yyyy-MM-dd'T'HH:mm:ss"));

    }

    @Test
    void submit_payment_returns400_when_purchase_amount_invalid() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"payment for stuff fdafsdfasasssssssssssss\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": null}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("purchaseAmount").value("Purchase amount is required"));

    }

    @Test
    void submit_payment_returns400_when_purchase_amount_negative() throws Exception {
        mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": -1}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("purchaseAmount").value("Purchase amount must be positive"));

    }

}