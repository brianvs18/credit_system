package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditServices {
    private final CreditRepository creditRepository;

    public Flux<CreditDTO> findAll() {
        return creditRepository.findAll()
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .creationDate(creditDocument.getCreationDate())
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(creditDocument.getStatus())
                        .build());
    }

    public Mono<CreditDTO> findById(String creditId) {
        return creditRepository.findById(creditId)
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .creationDate(creditDocument.getCreationDate())
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(creditDocument.getStatus())
                        .build());
    }

    public Flux<CreditDTO> findAllByUserIdentification(Integer userIdentification) {
        return creditRepository.findAllByUserIdentification(userIdentification)
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .creationDate(creditDocument.getCreationDate())
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(creditDocument.getStatus())
                        .build());
    }
}
