package com.twaszak.payments.dto;

import com.twaszak.payments.model.PurchaseTransaction;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseTransactionDTO {

    @NotBlank(message = "Description for purchase cannot be empty")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @NotNull(message = "Date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime purchaseTransactionDate;

    @NotNull(message = "Purchase amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Purchase amount must be positive")
    private BigDecimal purchaseAmount;


    public PurchaseTransaction toPayment(){
        return PurchaseTransaction.builder()
                .description(description)
                .purchaseTransactionDate(purchaseTransactionDate.atZone(ZoneId.of("UTC")))
                .purchaseAmount(purchaseAmount.setScale(2, RoundingMode.HALF_UP)).build();
    }
}
