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


        if((currencyConversionDTO.getCountry() == null || currencyConversionDTO.getCountry().isEmpty()) &&  (currencyConversionDTO.getCurrency() == null || currencyConversionDTO.getCurrency().isEmpty() )) {
            return false;
        }

        return true;
    }
}