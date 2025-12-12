package com.twaszak.payments.model;

import com.twaszak.payments.validators.PurchaseAmountConstraint;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Data
@NoArgsConstructor // Required by JPA
@AllArgsConstructor
@Entity
@SuperBuilder
public class PurchaseTransaction {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Description for purchase cannot be empty")
    @Size(max = 50, message = "Description must not exceed 50 characters")
    private String description;

    @NotNull(message = "Date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime purchaseTransactionDate;
    /*
     * Purchase amount is in USD
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "Purchase amount must be positive")
    @PurchaseAmountConstraint
    private BigDecimal purchaseAmount;
}
