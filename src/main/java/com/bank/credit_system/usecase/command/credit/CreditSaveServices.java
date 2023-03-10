package com.bank.credit_system.usecase.command.credit;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.entity.CreditDocument;
import com.bank.credit_system.enums.CreditErrorEnum;
import com.bank.credit_system.enums.CreditStatusEnum;
import com.bank.credit_system.exceptions.CreditException;
import com.bank.credit_system.repository.CreditRepository;
import com.bank.credit_system.usecase.handler.AccountServices;
import com.bank.credit_system.usecase.handler.CreditServices;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreditSaveServices {
    private final CreditRepository creditRepository;
    private final AccountServices accountServices;
    private final CreditServices creditServices;
    private static final Double PERCENTAGE_DEBT = 0.40;
    private static final Integer MIN_VALUE = 111111;
    private static final Integer MAX_VALUE = 999999;

    public Mono<CreditDTO> saveCredit(CreditDTO creditDTO) {
        return Mono.just(creditDTO)
                .flatMap(creditData -> accountServices.findByUserIdentification(creditDTO.getUserIdentification())
                        .flatMap(accountDTO -> validateUserIncome(creditData, accountDTO))
                        .flatMap(creditDataDTO -> Mono.just(creditDataDTO)
                                .filter(credit -> Objects.nonNull(credit.getId()))
                                .flatMap(credit -> creditRepository.findById(creditDTO.getId())
                                        .map(creditDocument -> creditDocument.toBuilder()
                                                .id(creditDocument.getId())
                                                .creditNumber(creditDocument.getCreditNumber())
                                                .creationDate(creditDocument.getCreationDate())
                                                .numberInstallments(creditDocument.getNumberInstallments())
                                                .creditValue(creditDTO.getCreditValue())
                                                .monthlyFee(creditDocument.getMonthlyFee())
                                                .userIdentification(creditDocument.getUserIdentification())
                                                .status(creditDocument.getStatus())
                                                .build()))
                                .switchIfEmpty(Mono.defer(() -> Mono.just(creditDataDTO)
                                        .map(credit -> CreditDocument.builder()
                                                .creditValue(credit.getCreditValue())
                                                .creditNumber(RandomUtils.nextInt(MIN_VALUE, MAX_VALUE))
                                                .numberInstallments(credit.getNumberInstallments())
                                                .monthlyFee(credit.getMonthlyFee())
                                                .userIdentification(credit.getUserIdentification())
                                                .creationDate(Clock.systemDefaultZone().millis())
                                                .status(CreditStatusEnum.ACTIVE.getId())
                                                .build())))
                                .flatMap(creditRepository::save)
                                .thenReturn(creditDataDTO)));
    }

    private Mono<CreditDTO> validateUserIncome(CreditDTO creditDTO, AccountDTO accountDTO) {
        return Mono.just(creditDTO)
                .filter(credit -> Objects.nonNull(creditDTO.getExpenses()))
                .flatMap(creditData -> Mono.just(creditData)
                        .filter(creditDataDTO -> accountDTO.getIncome() > creditDTO.getExpenses())
                        .flatMap(creditDataDTO -> validateUserDebtCapacity(creditDataDTO, accountDTO))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENDITURES_EXCEED_INCOME)))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENSES_IS_REQUIRED))));
    }

    private Mono<CreditDTO> validateUserDebtCapacity(CreditDTO creditDTO, AccountDTO accountDTO) {
        return buildMonthlyFee(creditDTO)
                .flatMap(creditData -> findCreditsByUserAndCalculateTotalBorrowingCapacity(creditData)
                        .switchIfEmpty(Mono.defer(() -> Mono.just(creditData)
                                .map(credit -> credit.toBuilder()
                                        .totalBorrowingCapacity(credit.getMonthlyFee() + creditDTO.getExpenses())
                                        .build()))))
                .filter(userCredit -> userCredit.getTotalBorrowingCapacity() < (accountDTO.getIncome() * PERCENTAGE_DEBT))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.DOES_NOT_MEET_DEBT_CAPACITY))));
    }

    private Mono<CreditDTO> findCreditsByUserAndCalculateTotalBorrowingCapacity(CreditDTO creditData) {
        return creditServices.findAllByUserIdentification(creditData.getUserIdentification())
                .filter(creditDB -> !CreditStatusEnum.CANCEL.getId().equals(creditDB.getStatus()))
                .collectList()
                .filter(creditDTOS -> !creditDTOS.isEmpty())
                .map(creditDTOS -> creditData.toBuilder()
                        .totalBorrowingCapacity(calculateTotalCapacity(creditData, creditDTOS))
                        .build());
    }

    private Mono<CreditDTO> buildMonthlyFee(CreditDTO creditDTO) {
        return Mono.just(creditDTO)
                .map(credit -> credit.toBuilder()
                        .monthlyFee(creditDTO.getCreditValue() / creditDTO.getNumberInstallments())
                        .build());
    }

    private Double calculateTotalCapacity(CreditDTO creditData, List<CreditDTO> creditDTOS) {
        return creditDTOS.stream()
                .mapToDouble(CreditDTO::getMonthlyFee).sum() + creditData.getExpenses() + creditData.getMonthlyFee();
    }

    public Mono<CreditDTO> editCreditBeforePayment(CreditDTO creditDTO, Double payment) {
        return Mono.just(creditDTO)
                .flatMap(creditData -> creditRepository.findById(creditDTO.getId())
                        .map(creditDB -> CreditDocument.builder()
                                .id(creditDB.getId())
                                .creditValue(creditDB.getCreditValue() - payment)
                                .numberInstallments(creditDTO.getNumberInstallments() - NumberUtils.INTEGER_ONE)
                                .monthlyFee(creditDB.getMonthlyFee())
                                .creditNumber(creditDB.getCreditNumber())
                                .userIdentification(creditDB.getUserIdentification())
                                .status(creditDB.getStatus())
                                .creationDate(creditDB.getCreationDate())
                                .build())
                        .flatMap(creditDocument -> Mono.just(creditDocument)
                                .filter(creditDB -> Objects.equals(creditDB.getCreditValue(), NumberUtils.DOUBLE_ZERO))
                                .map(credit -> credit.toBuilder()
                                        .numberInstallments(NumberUtils.INTEGER_ZERO)
                                        .status(CreditStatusEnum.CANCEL.getId())
                                        .build())
                                .defaultIfEmpty(creditDocument))
                        .flatMap(creditRepository::save)
                        .thenReturn(creditDTO));
    }
}
