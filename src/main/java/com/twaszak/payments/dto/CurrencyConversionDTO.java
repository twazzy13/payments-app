package com.twaszak.payments.dto;

import com.twaszak.payments.dto.validators.CurrencyConversionConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CurrencyConversionConstraint
public class CurrencyConversionDTO {

    private Optional<String> country;
    private Optional<String> currency;
}
