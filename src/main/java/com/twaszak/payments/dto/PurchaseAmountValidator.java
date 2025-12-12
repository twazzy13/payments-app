package com.twaszak.payments.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class PurchaseAmountValidator implements ConstraintValidator<PurchaseAmountConstraint, BigDecimal> {

    @Override
    public void initialize(PurchaseAmountConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(BigDecimal purchaseAmount, ConstraintValidatorContext cxt) {
        return purchaseAmount != null && purchaseAmount.scale() == 2;
    }
}
