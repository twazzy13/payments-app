package com.twaszak.payments.service;

import com.twaszak.payments.dto.PurchaseTransactionDTO;
import com.twaszak.payments.model.PurchaseTransaction;
import com.twaszak.payments.repository.PurchaseTranscationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service class for managing purchase transations.
 */
@Service
public class PurchaseTransactionService {

    Logger logger = LoggerFactory.getLogger(PurchaseTransactionService.class);

    private final PurchaseTranscationRepository repository;

    public PurchaseTransactionService(PurchaseTranscationRepository repository) {
        this.repository = repository;
    }

    /**
     *
     * This method submits a purchase transaction with purchase amount in USD
     *
     * @param {@link PurchaseTransactionDTO}
     * @return {@link PurchaseTransaction}
     */
    public PurchaseTransaction submitTransaction(PurchaseTransactionDTO transaction)
    {
        logger.debug("Submitting transaction: " + transaction);
        PurchaseTransaction purchaseTransaction = repository.save(transaction.toPayment());
        logger.debug("Transaction submitted: " + purchaseTransaction);
        return purchaseTransaction;
    }

}
