package com.bank.credit_system.repository;

import com.bank.credit_system.entity.PaymentDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PaymentRepository extends ReactiveMongoRepository<PaymentDocument, String> {
}
