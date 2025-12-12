package com.twaszak.payments.dto;

import com.twaszak.payments.validators.CurrencyConversionConstraint;
import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CurrencyConversionConstraint
@Getter
public class CurrencyConversionDTO {

    private Optional<String> country;
    private Optional<String> currency;
}
