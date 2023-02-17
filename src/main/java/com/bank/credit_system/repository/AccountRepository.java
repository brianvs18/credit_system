package com.bank.credit_system.repository;

import com.bank.credit_system.entity.AccountDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<AccountDocument, String> {
    Mono<AccountDocument> findByUserIdentification(Integer userIdentification);
}
