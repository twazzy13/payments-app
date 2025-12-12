package com.twaszak.payments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CurrencyConversionResponse {

    private Long id;
    private String description;
    private LocalDateTime purchaseTransactionDate;
    private BigDecimal purchaseAmountUSD;
    private BigDecimal exchangeRate;
    private BigDecimal convertedPurchaseAmount;
}
