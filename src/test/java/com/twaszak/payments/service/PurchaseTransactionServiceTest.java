package com.twaszak.payments.service;

import com.twaszak.payments.dto.CurrencyConversionDTO;
import com.twaszak.payments.dto.CurrencyConversionResponse;
import com.twaszak.payments.dto.PurchaseTransactionDTO;
import com.twaszak.payments.exceptions.NoCurrencyDataException;
import com.twaszak.payments.exceptions.NoTransactionPresent;
import com.twaszak.payments.model.PurchaseTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(
        locations = "classpath:application-test.properties")
class PurchaseTransactionServiceTest {

    @Autowired
    PurchaseTransactionService service;

    @Test
    void testAddTransaction() {
        PurchaseTransactionDTO paymentDTO = PurchaseTransactionDTO.builder()
                .purchaseAmount(new BigDecimal(30.3453))
                .description("payment")
                .purchaseTransactionDate(LocalDateTime.of(2025, 9, 30, 0, 0, 0))
                .build();



        PurchaseTransaction payment = service.addTransaction(paymentDTO);

        assertEquals(2, payment.getPurchaseAmount().scale());
        assertEquals(new BigDecimal(30.35).setScale(2, RoundingMode.HALF_UP), payment.getPurchaseAmount());
        assertEquals(paymentDTO.getDescription(), payment.getDescription());
        assertEquals(paymentDTO.getPurchaseTransactionDate().toInstant(ZoneOffset.UTC), payment.getPurchaseTransactionDate().toInstant());
        assertNotNull(payment.getId());
    }

    @Test
    void testgetConvertedPurchaseTransaction() throws NoCurrencyDataException, NoTransactionPresent {
        PurchaseTransactionDTO paymentDTO = PurchaseTransactionDTO.builder()
                .purchaseAmount(new BigDecimal(30.3453))
                .description("payment")
                .purchaseTransactionDate(LocalDateTime.of(2025, 9, 30, 0, 0, 0))
                .build();



        PurchaseTransaction payment = service.addTransaction(paymentDTO);

        assertEquals(2, payment.getPurchaseAmount().scale());
        assertEquals(new BigDecimal(30.35).setScale(2, RoundingMode.HALF_UP), payment.getPurchaseAmount());
        assertEquals(paymentDTO.getDescription(), payment.getDescription());
        assertEquals(paymentDTO.getPurchaseTransactionDate().toInstant(ZoneOffset.UTC), payment.getPurchaseTransactionDate().toInstant());
        assertNotNull(payment.getId());


        CurrencyConversionResponse response = service.getConvertedPurchaseTransaction(payment.getId(), CurrencyConversionDTO.builder().currency("Euro").build());

        assertEquals(2, payment.getPurchaseAmount().scale());
        assertEquals(new BigDecimal(30.35).setScale(2, RoundingMode.HALF_UP), response.getPurchaseAmountUSD());
        assertEquals(paymentDTO.getDescription(), response.getDescription());
        assertEquals(paymentDTO.getPurchaseTransactionDate(), response.getPurchaseTransactionDate());
        assertEquals(payment.getId(), response.getId());
        assertEquals(new BigDecimal(.852).setScale(3, RoundingMode.HALF_UP),response.getExchangeRate());
        assertEquals(2, response.getConvertedPurchaseAmount().scale());
        assertEquals(new BigDecimal(25.86).setScale(2, RoundingMode.HALF_UP),response.getConvertedPurchaseAmount());
    }
}