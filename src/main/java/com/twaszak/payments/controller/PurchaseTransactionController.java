package com.twaszak.payments.controller;

import com.twaszak.payments.dto.CurrencyConversionDTO;
import com.twaszak.payments.dto.CurrencyConversionResponse;
import com.twaszak.payments.dto.PurchaseTransactionDTO;
import com.twaszak.payments.dto.PurchaseTransactionResponse;
import com.twaszak.payments.exceptions.NoCurrencyDataException;
import com.twaszak.payments.exceptions.NoTransactionPresent;
import com.twaszak.payments.service.PurchaseTransactionService;
import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



/**
 * Controller class for handling web requests related to payments
 * This class provides RESTful endpoints for storing payment
 *
 * @author Tim Waszak
 * @since 0.0.1
 * @see PurchaseTransactionService
 */
@RestController
@RequestMapping("/api/v1")
public class PurchaseTransactionController {

    private final Logger logger = LoggerFactory.getLogger(PurchaseTransactionController.class);
    private final PurchaseTransactionService transactionService;

    @Autowired
    public PurchaseTransactionController(final PurchaseTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * This controller method submits and stores a {@link PurchaseTransactionDTO} which needs to have a description, purchase amount in USD, and payment date in UTC
     *
     * @param  {@link PaymentDTO}
     * @return {@link PurchaseTransactionResponse}
     */
    @PostMapping("/purchase")
    public ResponseEntity<PurchaseTransactionResponse> addPurchaseTransaction(@Valid @RequestBody PurchaseTransactionDTO paymentDTO) {
        logger.debug("Received request to submit payment: {}", paymentDTO);

        try {
           return ResponseEntity.ok(new PurchaseTransactionResponse( transactionService.addTransaction(paymentDTO)));
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        finally {
            logger.debug("Received request to submit payment: {}", paymentDTO);
        }
    }

    @GetMapping("/purchase/{id}")
    public ResponseEntity getPurchaseTransaction(@PathVariable Long id, @Valid @RequestBody CurrencyConversionDTO currencyConversionDTO) {


        logger.debug("Received request to submit payment: {}", currencyConversionDTO);
        try {
            return ResponseEntity.ok(transactionService.getConvertedPurchaseTransaction(id,currencyConversionDTO));
        }
        catch (NoCurrencyDataException ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No currency data for country: %s or currency: %s could be found"
                            .formatted(currencyConversionDTO.getCountry(), currencyConversionDTO.getCurrency()));
        }
        catch (NoTransactionPresent e)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No transaction data with id: %s could be found"
                            .formatted(id));
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        finally {
            logger.debug("Received request to submit payment: {}", currencyConversionDTO);
        }
    }

}
