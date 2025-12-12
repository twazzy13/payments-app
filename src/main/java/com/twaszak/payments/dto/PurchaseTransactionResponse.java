package com.twaszak.payments.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.twaszak.payments.model.PurchaseTransaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@Data
public class PurchaseTransactionResponse {

    private Long id;
    private String description;
    private LocalDateTime purchaseTransactionDate;
    private BigDecimal purchaseAmount;

    public PurchaseTransactionResponse(PurchaseTransaction purchaseTransaction) {
        this.id = purchaseTransaction.getId();
        this.description = purchaseTransaction.getDescription();
        this.purchaseTransactionDate = purchaseTransaction.getPurchaseTransactionDate().toLocalDateTime();
        this.purchaseAmount = purchaseTransaction.getPurchaseAmount();
    }
}
