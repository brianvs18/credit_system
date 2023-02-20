package com.bank.credit_system.controller;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.usecase.command.account.AccountDeleteServices;
import com.bank.credit_system.usecase.command.account.AccountSaveServices;
import com.bank.credit_system.usecase.handler.AccountServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts")
public class AccountController {
    private final AccountServices accountServices;
    private final AccountSaveServices accountSaveServices;
    private final AccountDeleteServices accountDeleteServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<AccountDTO> findAll() {
        return accountServices.findAll();
    }

    @GetMapping(path = "/account", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<AccountDTO> findById(@RequestParam(value = "accountId") final String accountId) {
        return accountServices.findById(accountId);
    }

    @PostMapping(path = "/account")
    public Mono<AccountDTO> saveAccount(@RequestBody AccountDTO accountDTO) {
        return accountSaveServices.saveAccount(accountDTO);
    }

    @DeleteMapping(path = "/account")
    public Mono<Void> deleteAccount(@RequestParam(value = "accountId") final String accountId) {
        return accountDeleteServices.deleteAccount(accountId);
    }
}
