package com.bank.credit_system.repository;

import com.bank.credit_system.entity.CreditDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditRepository extends ReactiveMongoRepository<CreditDocument, String> {
    Flux<CreditDocument> findAllByUserIdentification(Integer userIdentification);
    Mono<CreditDocument> findByCreditNumber(Integer creditNumber);
}
