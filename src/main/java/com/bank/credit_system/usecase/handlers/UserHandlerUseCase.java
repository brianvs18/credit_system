package com.bank.credit_system.usecase.handlers;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserHandlerUseCase {
    private final UserRepository userRepository;

    public Flux<UserDTO> findAll(){
        return userRepository.findAll()
                .map(userDocument -> UserDTO.builder()
                        .id(userDocument.getId())
                        .name(userDocument.getName())
                        .lastname(userDocument.getLastname())
                        .age(userDocument.getAge())
                        .build());
    }

    public Mono<UserDTO> findById(String userId){
        return userRepository.findById(userId)
                .map(userDocument -> UserDTO.builder()
                        .id(userDocument.getId())
                        .name(userDocument.getName())
                        .lastname(userDocument.getLastname())
                        .age(userDocument.getAge())
                        .build());
    }
}
