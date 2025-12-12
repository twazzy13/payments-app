package com.twaszak.payments.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    void submit_payment_and_convert_currency_for_country() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-09-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmount").value(9303.33))
                .andExpect(jsonPath("id").isNotEmpty()).andReturn();


        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "id");


        mockMvc.perform(get("/api/v1/purchase/{id}", id)
                        .content("{\"country\": \"Afghanistan\", \"currency\": \"\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-09-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmountUSD").value(9303.33))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("exchangeRate").value(67.33))
                .andExpect(jsonPath("convertedPurchaseAmount").value(626393.21));


    }
    @Test
    void submit_payment_and_convert_currency_for_currency() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-09-30T00:00:00\",\"purchaseAmount\": 1000.50}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-09-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmount").value(1000.50))
                .andExpect(jsonPath("id").isNotEmpty()).andReturn();


        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "id");


        mockMvc.perform(get("/api/v1/purchase/{id}", id)
                        .content("{\"country\": \"\", \"currency\": \"Euro\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-09-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmountUSD").value(1000.50))
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("exchangeRate").value(0.852))
                .andExpect(jsonPath("convertedPurchaseAmount").value(852.43));


    }
    @Test
    void submit_payment_and_convert_currency_with_no_exchange_rate_existing_for_country() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-06-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-06-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmount").value(9303.33))
                .andExpect(jsonPath("id").isNotEmpty()).andReturn();


        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "id");


        mockMvc.perform(get("/api/v1/purchase/{id}", id)
                        .content("{\"country\": \"Afghanistans\", \"currency\": \"\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("No currency data for country: Afghanistans or currency:  could be found"));

    }

    @Test
    void submit_payment_and_convert_currency_with_no_exchange_rate_existing_for_currency() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/v1/purchase")
                        .content("{\"description\": \"test\", \"purchaseTransactionDate\": \"2025-06-30T00:00:00\",\"purchaseAmount\": 9303.325}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("description").value("test"))
                .andExpect(jsonPath("purchaseTransactionDate").value("2025-06-30T00:00:00"))
                .andExpect(jsonPath("purchaseAmount").value(9303.33))
                .andExpect(jsonPath("id").isNotEmpty()).andReturn();


        Integer id = JsonPath.read(result.getResponse().getContentAsString(), "id");


        mockMvc.perform(get("/api/v1/purchase/{id}", id)
                        .content("{\"country\": \"\", \"currency\": \"EUROTest\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("No currency data for country:  or currency: EUROTest could be found"));

    }
}