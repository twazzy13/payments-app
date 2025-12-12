package com.twaszak.payments.service;

import com.twaszak.payments.dto.CurrencyConversionDTO;
import com.twaszak.payments.dto.CurrencyConversionResponse;
import com.twaszak.payments.dto.PurchaseTransactionDTO;
import com.twaszak.payments.exceptions.NoCurrencyDataException;
import com.twaszak.payments.exceptions.NoTransactionPresent;
import com.twaszak.payments.model.PurchaseTransaction;
import com.twaszak.payments.repository.PurchaseTranscationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

/**
 * Service class for managing purchase transations.
 */
@Service
public class PurchaseTransactionService {

    Logger logger = LoggerFactory.getLogger(PurchaseTransactionService.class);

    private final PurchaseTranscationRepository repository;

    private final CurrencyConversionService currencyConversionService;

    public PurchaseTransactionService(PurchaseTranscationRepository repository, CurrencyConversionService currencyConversionService) {
        this.repository = repository;
        this.currencyConversionService = currencyConversionService;
    }

    /**
     *
     * This method submits a purchase transaction with purchase amount in USD
     *
     * @param {@link PurchaseTransactionDTO}
     * @return {@link PurchaseTransaction}
     */
    public PurchaseTransaction addTransaction(PurchaseTransactionDTO transaction)
    {
        logger.debug("Submitting transaction: " + transaction);
        PurchaseTransaction purchaseTransaction = repository.save(transaction.toPayment());
        logger.debug("Transaction submitted: " + purchaseTransaction);
        return purchaseTransaction;
    }


    public CurrencyConversionResponse getConvertedPurchaseTransaction(Long id, CurrencyConversionDTO currencyConversionDTO) throws NoCurrencyDataException, NoTransactionPresent {
        PurchaseTransaction t = repository.findById(id).orElseThrow(NoTransactionPresent::new);
        return  currencyConversionService.getConvertedPurchaseTransaction(t,currencyConversionDTO);
    }

}
