package com.twaszak.payments.validators;

import com.twaszak.payments.dto.CurrencyConversionDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CurrencyConversionValidator implements ConstraintValidator<CurrencyConversionConstraint, CurrencyConversionDTO> {

    @Override
    public void initialize(CurrencyConversionConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(CurrencyConversionDTO currencyConversionDTO, ConstraintValidatorContext cxt) {


        return !(currencyConversionDTO.getCountry().isEmpty() && currencyConversionDTO.getCountry().get().isEmpty())
                || !currencyConversionDTO.getCurrency().isEmpty();
    }
}