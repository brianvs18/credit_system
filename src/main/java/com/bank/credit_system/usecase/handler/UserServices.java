package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.UserException;
import com.bank.credit_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final UserRepository userRepository;

    public Flux<UserDTO> findAll() {
        return userRepository.findAll()
                .map(userDocument -> UserDTO.builder()
                        .id(userDocument.getId())
                        .name(userDocument.getName())
                        .lastname(userDocument.getLastname())
                        .identification(userDocument.getIdentification())
                        .age(userDocument.getAge())
                        .build());
    }

    public Mono<UserDTO> findById(String userId) {
        return userRepository.findById(userId)
                .map(userDocument -> UserDTO.builder()
                        .id(userDocument.getId())
                        .name(userDocument.getName())
                        .lastname(userDocument.getLastname())
                        .identification(userDocument.getIdentification())
                        .age(userDocument.getAge())
                        .build());
    }

    public Mono<UserDTO> findByIdentification(String identification) {
        return userRepository.findByIdentification(identification)
                .map(userDocument -> UserDTO.builder()
                        .id(userDocument.getId())
                        .name(userDocument.getName())
                        .lastname(userDocument.getLastname())
                        .identification(userDocument.getIdentification())
                        .age(userDocument.getAge())
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.USER_IS_NOT_EXISTS))));
    }
}
