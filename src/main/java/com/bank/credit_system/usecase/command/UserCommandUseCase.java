package com.bank.credit_system.usecase.command;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.entity.UserDocument;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.UserException;
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
                .flatMap(this::validateIdentificationNotRegistered)
                .filter(userData -> Objects.nonNull(userDTO.getId()))
                .flatMap(userData -> userRepository.findById(userDTO.getId())
                        .filter(userDocument -> userDocument.getIdentification().equals(userDTO.getIdentification()))
                        .map(userDocument -> UserDocument.builder()
                                .id(userDocument.getId())
                                .name(userData.getName())
                                .lastname(userData.getLastname())
                                .identification(userDocument.getIdentification())
                                .age(userData.getAge())
                                .build())
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.EDITING_IDENTIFICATION_NOT_ALLOWED)))))
                .switchIfEmpty(Mono.defer(() -> Mono.just(userDTO))
                        .map(userData -> UserDocument.builder()
                                .name(userData.getName())
                                .lastname(userData.getLastname())
                                .identification(userData.getIdentification())
                                .age(userData.getAge())
                                .build()))
                .flatMap(userRepository::save)
                .thenReturn(userDTO);
    }

    private Mono<UserDTO> validateIdentificationNotRegistered(UserDTO userDTO) {
        return Mono.just(userDTO)
                .flatMap(userData -> userRepository.findByIdentification(userDTO.getIdentification())
                        .flatMap(userDocument -> Mono.just(userDocument)
                                .filter(userDB -> userDocument.getId().equals(userDTO.getId()))
                                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.IDENTIFICATION_ALREADY_EXISTS)))))
                        .then())
                .thenReturn(userDTO);
    }

    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(userId);
    }
}
