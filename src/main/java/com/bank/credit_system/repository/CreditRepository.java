package com.bank.credit_system.repository;

import com.bank.credit_system.entity.CreditDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {
}
