package com.bank.credit_system.repository;

import com.bank.credit_system.entity.UserDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<UserDocument, String> {
}
