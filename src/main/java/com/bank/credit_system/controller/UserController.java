package com.bank.credit_system.controller;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.usecase.command.UserCommandUseCase;
import com.bank.credit_system.usecase.handler.UserHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserHandlerUseCase userHandlerUseCase;
    private final UserCommandUseCase userCommandUseCase;
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<UserDTO> findAll(){
        return userHandlerUseCase.findAll();
    }

    @GetMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> findById(@RequestParam(value = "userId") final String userId){
        return userHandlerUseCase.findById(userId);
    }

    @PostMapping("/user")
    public Mono<UserDTO> saveUser(@RequestBody UserDTO userDTO){
        return userCommandUseCase.saveUser(userDTO);
    }

    @DeleteMapping("/user")
    public Mono<Void> deleteUser(@RequestParam(value = "userId") final String userId){
        return userCommandUseCase.deleteUser(userId);
    }
}
