package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.enums.CreditErrorEnum;
import com.bank.credit_system.enums.CreditStatusEnum;
import com.bank.credit_system.exceptions.CreditException;
import com.bank.credit_system.functions.DateFunction;
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
                        .creditNumber(creditDocument.getCreditNumber())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .monthlyFee(creditDocument.getMonthlyFee())
                        .creationDate(DateFunction.getDateFromMillis(creditDocument.getCreationDate()))
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(CreditStatusEnum.nameFromId(creditDocument.getStatus()))
                        .build());
    }

    public Mono<CreditDTO> findById(String creditId) {
        return creditRepository.findById(creditId)
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditNumber(creditDocument.getCreditNumber())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .monthlyFee(creditDocument.getMonthlyFee())
                        .creationDate(DateFunction.getDateFromMillis(creditDocument.getCreationDate()))
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(CreditStatusEnum.nameFromId(creditDocument.getStatus()))
                        .build());
    }

    public Flux<CreditDTO> findAllByUserIdentification(String userIdentification) {
        return creditRepository.findAllByUserIdentification(userIdentification)
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditNumber(creditDocument.getCreditNumber())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .monthlyFee(creditDocument.getMonthlyFee())
                        .creationDate(DateFunction.getDateFromMillis(creditDocument.getCreationDate()))
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(creditDocument.getStatus())
                        .build());
    }

    public Mono<CreditDTO> findByCreditNumber(Integer creditNumber) {
        return creditRepository.findByCreditNumber(creditNumber)
                .map(creditDocument -> CreditDTO.builder()
                        .id(creditDocument.getId())
                        .creditNumber(creditDocument.getCreditNumber())
                        .creditValue(creditDocument.getCreditValue())
                        .numberInstallments(creditDocument.getNumberInstallments())
                        .monthlyFee(creditDocument.getMonthlyFee())
                        .creationDate(DateFunction.getDateFromMillis(creditDocument.getCreationDate()))
                        .userIdentification(creditDocument.getUserIdentification())
                        .status(creditDocument.getStatus())
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.CREDIT_NUMBER_NOT_EXISTS))));
    }
}
