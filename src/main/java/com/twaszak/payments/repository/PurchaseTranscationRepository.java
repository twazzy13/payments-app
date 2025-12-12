package com.twaszak.payments.repository;

import com.twaszak.payments.model.PurchaseTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseTranscationRepository extends CrudRepository<PurchaseTransaction, Long> {

}
