package com.twaszak.payments.dto.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyConversionValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyConversionConstraint {
    String message() default "One of either country or currency must be populated";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
