package com.twaszak.payments.service;

import com.twaszak.payments.dto.CurrencyConversionDTO;
import com.twaszak.payments.dto.CurrencyConversionResponse;
import com.twaszak.payments.exceptions.NoCurrencyDataException;
import com.twaszak.payments.model.CurrencyConversionDetails;
import com.twaszak.payments.model.PurchaseTransaction;
import com.twaszak.payments.provider.FiscalDataProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

/**
 * This service is responsible for grabbing currency conversion data for a purchase transaction and providing a currency conversion from USD to the respective country or currency.
 */
@Service
public class CurrencyConversionService {

    FiscalDataProvider fiscalDataRepository;

    public CurrencyConversionService(FiscalDataProvider fiscalDataRepository) {
        this.fiscalDataRepository = fiscalDataRepository;
    }

    /**
     * This application returns a
     * @param {@link PurchaseTransaction}
     * @param {@link CurrencyConversionDTO}
     * @return {@link @CurrencyConversionResponse}
     * @throws NoCurrencyDataException
     */
    public CurrencyConversionResponse getConvertedPurchaseTransaction(PurchaseTransaction purchaseTransaction, CurrencyConversionDTO currencyConversionDTO) throws NoCurrencyDataException {

        // get currency exchange rates for the last six months.
        List<CurrencyConversionDetails> details = fiscalDataRepository.getConversionForLastSixMonths(purchaseTransaction.getPurchaseTransactionDate(),
                currencyConversionDTO.getCountry(),currencyConversionDTO.getCurrency());

        //get the most recent conversions
        CurrencyConversionDetails detail = details.stream().max(Comparator.comparing(CurrencyConversionDetails::getRecordDate)).get();


        //convert usd to currency amount.
        BigDecimal conversion = purchaseTransaction.getPurchaseAmount().multiply(detail.getExchangeRate()).setScale(2, RoundingMode.HALF_UP);

        return CurrencyConversionResponse.builder()
                    .id(purchaseTransaction.getId())
                    .description(purchaseTransaction.getDescription())
                    .purchaseTransactionDate(purchaseTransaction.getPurchaseTransactionDate().toLocalDateTime())
                    .purchaseAmountUSD(purchaseTransaction.getPurchaseAmount())
                    .convertedPurchaseAmount(conversion)
                    .exchangeRate(detail.getExchangeRate()).build();


    }

}
