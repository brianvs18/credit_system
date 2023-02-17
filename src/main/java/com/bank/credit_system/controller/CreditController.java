package com.bank.credit_system.controller;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.usecase.handler.CreditHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/credits")
public class CreditController {
    private final CreditHandlerUseCase creditHandlerUseCase;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<CreditDTO> findAll(){
        return creditHandlerUseCase.findAll();
    }

    @GetMapping(path = "/credit", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<CreditDTO> findById(@RequestParam(value = "creditId") final String creditId){
        return creditHandlerUseCase.findById(creditId);
    }

    @PostMapping(path = "/credit")
    public Mono<CreditDTO> saveCredit(@RequestBody CreditDTO creditDTO){
        return null;
    }

    @DeleteMapping(path = "/credit")
    public Mono<Void> deleteCredit(@RequestParam(value = "creditId") final String creditId){
        return null;
    }
}
