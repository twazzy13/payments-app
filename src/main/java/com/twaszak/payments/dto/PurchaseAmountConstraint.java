package com.twaszak.payments.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PurchaseAmountValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PurchaseAmountConstraint {
    String message() default "Purchase amount cannot be null and at most have 2 decimal places";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
