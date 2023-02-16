package com.bank.credit_system.usecase.command;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.entity.UserDocument;
import com.bank.credit_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserCommandUseCase {
    private final UserRepository userRepository;

    public Mono<UserDTO> saveUser(UserDTO userDTO) {
        return Mono.just(userDTO)
                .filter(userData -> Objects.nonNull(userDTO.getId()))
                .flatMap(userData -> userRepository.findById(userDTO.getId())
                        .map(userDocument -> UserDocument.builder()
                                .id(userDocument.getId())
                                .name(userData.getName())
                                .lastname(userData.getLastname())
                                .age(userData.getAge())
                                .build()))
                .switchIfEmpty(Mono.defer(() -> Mono.just(userDTO))
                        .map(userData -> UserDocument.builder()
                                .name(userData.getName())
                                .lastname(userData.getLastname())
                                .age(userData.getAge())
                                .build()))
                .flatMap(userRepository::save)
                .thenReturn(userDTO);
    }

    public Mono<Void> deleteUser(String userId){
        return userRepository.deleteById(userId);
    }
}
