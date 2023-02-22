package com.bank.credit_system.controller.controlleradvice;

import com.bank.credit_system.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(value = RuntimeException.class)
    public Mono<ErrorDTO> runTimeExceptionHandler(RuntimeException exception) {
        return Mono.just(exception)
                .map(exception1 -> ErrorDTO.builder()
                        .requestId(UUID.randomUUID().toString().replace("-", ""))
                        .message(exception.getMessage())
                        .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                        .build());
    }
}
