package com.twaszak.payments.controller;

import com.twaszak.payments.dto.PurchaseTransactionDTO;
import com.twaszak.payments.dto.PurchaseTransactionResponse;
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
    @PostMapping("/submit")
    public ResponseEntity<PurchaseTransactionResponse> submitPayment(@Valid @RequestBody PurchaseTransactionDTO paymentDTO) {
        logger.debug("Received request to submit payment: {}", paymentDTO);

        try {
           return ResponseEntity.ok(new PurchaseTransactionResponse( transactionService.submitTransaction(paymentDTO)));
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        finally {
            logger.debug("Received request to submit payment: {}", paymentDTO);
        }
    }

}
