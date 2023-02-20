package com.bank.credit_system.controller;

import com.bank.credit_system.dto.UserDTO;
import com.bank.credit_system.usecase.command.user.UserDeleteServices;
import com.bank.credit_system.usecase.command.user.UserSaveServices;
import com.bank.credit_system.usecase.handler.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserServices userServices;
    private final UserSaveServices userSaveServices;
    private final UserDeleteServices userDeleteServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<UserDTO> findAll() {
        return userServices.findAll();
    }

    @GetMapping(path = "/user", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<UserDTO> findById(@RequestParam(value = "userId") final String userId) {
        return userServices.findById(userId);
    }

    @PostMapping("/user")
    public Mono<UserDTO> saveUser(@RequestBody UserDTO userDTO) {
        return userSaveServices.saveUser(userDTO);
    }

    @DeleteMapping("/user")
    public Mono<Void> deleteUser(@RequestParam(value = "userId") final String userId) {
        return userDeleteServices.deleteUser(userId);
    }
}
