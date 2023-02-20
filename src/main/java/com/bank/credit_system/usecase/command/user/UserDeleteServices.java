package com.bank.credit_system.usecase.command.user;

import com.bank.credit_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDeleteServices {
    private final UserRepository userRepository;

    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId);
    }
}
