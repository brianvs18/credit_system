package com.bank.credit_system.controller;

import com.bank.credit_system.dto.PaymentDTO;
import com.bank.credit_system.usecase.command.payment.PaymentSaveServices;
import com.bank.credit_system.usecase.handler.PaymentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/payments")
public class PaymentController {
    private final PaymentServices paymentServices;
    private final PaymentSaveServices paymentSaveServices;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<PaymentDTO> findAll() {
        return paymentServices.findAll();
    }

    @GetMapping(path = "/payment", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<PaymentDTO> findById(@RequestParam(value = "paymentId") final String paymentId) {
        return paymentServices.findById(paymentId);
    }

    @PostMapping(path = "/payment")
    public Mono<PaymentDTO> savePayment(@RequestBody PaymentDTO paymentDTO) {
        return paymentSaveServices.savePayment(paymentDTO);
    }
}
