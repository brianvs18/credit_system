package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.PaymentDTO;
import com.bank.credit_system.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentServices {
    private final PaymentRepository paymentRepository;

    public Flux<PaymentDTO> findAll() {
        return paymentRepository.findAll()
                .map(paymentDocument -> PaymentDTO.builder()
                        .id(paymentDocument.getId())
                        .creditNumber(paymentDocument.getCreditNumber())
                        .userIdentification(paymentDocument.getUserIdentification())
                        .payment(paymentDocument.getPayment())
                        .creationDate(paymentDocument.getCreationDate())
                        .build());
    }

    public Mono<PaymentDTO> findById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .map(paymentDocument -> PaymentDTO.builder()
                        .id(paymentDocument.getId())
                        .creditNumber(paymentDocument.getCreditNumber())
                        .userIdentification(paymentDocument.getUserIdentification())
                        .payment(paymentDocument.getPayment())
                        .creationDate(paymentDocument.getCreationDate())
                        .build());
    }
}
