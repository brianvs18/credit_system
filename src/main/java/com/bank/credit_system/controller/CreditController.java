package com.bank.credit_system.controller;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.usecase.command.credit.CreditDeleteServices;
import com.bank.credit_system.usecase.command.credit.CreditSaveServices;
import com.bank.credit_system.usecase.handler.CreditServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/credits")
public class CreditController {
    private final CreditServices creditServices;
    private final CreditSaveServices creditSaveServices;
    private final CreditDeleteServices creditDeleteServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<CreditDTO> findAll() {
        return creditServices.findAll();
    }

    @GetMapping(path = "/credit", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<CreditDTO> findById(@RequestParam(value = "creditId") final String creditId) {
        return creditServices.findById(creditId);
    }

    @PostMapping(path = "/credit")
    public Mono<CreditDTO> saveCredit(@RequestBody CreditDTO creditDTO) {
        return creditSaveServices.saveCredit(creditDTO);
    }

    @DeleteMapping(path = "/credit")
    public Mono<Void> deleteCredit(@RequestParam(value = "creditId") final String creditId) {
        return creditDeleteServices.deleteCredit(creditId);
    }
}
