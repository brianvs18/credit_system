package com.bank.credit_system.controller;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.usecase.command.AccountCommandUseCase;
import com.bank.credit_system.usecase.handler.AccountHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/accounts")
public class AccountController {
    private final AccountHandlerUseCase accountHandlerUseCase;
    private final AccountCommandUseCase accountCommandUseCase;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<AccountDTO> findAll(){
        return accountHandlerUseCase.findAll();
    }

    @GetMapping(path = "/account", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<AccountDTO> findById(@RequestParam(value = "accountId") final String accountId){
        return accountHandlerUseCase.findById(accountId);
    }

    @PostMapping(path = "/account")
    public Mono<AccountDTO> saveAccount(@RequestBody AccountDTO accountDTO){
        return accountCommandUseCase.saveAccount(accountDTO);
    }

    @DeleteMapping(path = "/account")
    public Mono<Void> deleteAccount(@RequestParam(value = "accountId") final String accountId){
        return accountCommandUseCase.deleteAccount(accountId);
    }
}
