package com.twaszak.payments.model;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ConvertedPaymentTransaction extends PurchaseTransaction {
    private BigDecimal exchangeRate;
    private BigDecimal paymentConversion;
}
