package com.bank.credit_system.usecase.command.credit;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.entity.CreditDocument;
import com.bank.credit_system.enums.AccountErrorEnum;
import com.bank.credit_system.enums.CreditErrorEnum;
import com.bank.credit_system.enums.CreditStatusEnum;
import com.bank.credit_system.exceptions.AccountErrorException;
import com.bank.credit_system.exceptions.CreditException;
import com.bank.credit_system.repository.CreditRepository;
import com.bank.credit_system.usecase.handler.AccountServices;
import com.bank.credit_system.usecase.handler.CreditServices;
import lombok.RequiredArgsConstructor;
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

    public Mono<CreditDTO> saveCredit(CreditDTO creditDTO) {
        return Mono.just(creditDTO)
                .flatMap(creditData -> validateUserHasAccount(creditDTO)
                        .flatMap(accountDTO -> validateUserIncome(creditData, accountDTO))
                        .flatMap(creditDataDTO -> Mono.just(creditDataDTO)
                                .filter(credit -> Objects.nonNull(credit.getId()))
                                .flatMap(credit -> creditRepository.findById(creditDTO.getId())
                                        .map(creditDocument -> creditDocument.toBuilder()
                                                .id(creditDocument.getId())
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
                                                .numberInstallments(credit.getNumberInstallments())
                                                .monthlyFee(credit.getMonthlyFee())
                                                .userIdentification(credit.getUserIdentification())
                                                .creationDate(Clock.systemDefaultZone().millis())
                                                .status(CreditStatusEnum.ACTIVE.getId())
                                                .build())))))
                .flatMap(creditRepository::save)
                .thenReturn(creditDTO);
    }

    private Mono<AccountDTO> validateUserHasAccount(CreditDTO creditDTO) {
        return accountServices.findByUserIdentification(creditDTO.getUserIdentification())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccountErrorException(AccountErrorEnum.USER_HAS_NO_REGISTERED_ACCOUNT))));
    }

    private Mono<CreditDTO> validateUserIncome(CreditDTO creditDTO, AccountDTO accountDTO) {
        return Mono.just(creditDTO)
                .filter(credit -> Objects.nonNull(creditDTO.getExpenses()))
                .flatMap(creditData -> Mono.just(creditData)
                        .filter(creditDataDTO -> accountDTO.getIncome() > creditDTO.getExpenses())
                        .flatMap(creditDataDTO -> validateUserDebtCapacity(creditDataDTO, accountDTO))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENDITURES_EXCEED_INCOME)))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENSES_ARE_REQUIRED))));
    }

    private Mono<CreditDTO> validateUserDebtCapacity(CreditDTO creditDTO, AccountDTO accountDTO) {
        return Mono.just(creditDTO)
                .map(credit -> credit.toBuilder()
                        .monthlyFee(creditDTO.getCreditValue() / creditDTO.getNumberInstallments())
                        .build())
                .flatMap(creditData -> creditServices.findAllByUserIdentification(creditData.getUserIdentification())
                        .collectList()
                        .filter(creditDTOS -> !creditDTOS.isEmpty())
                        .map(creditDTOS -> creditData.toBuilder()
                                .totalBorrowingCapacity(calculateTotalCapacity(creditData, creditDTOS))
                                .build())
                        .switchIfEmpty(Mono.defer(() -> Mono.just(creditData)
                                .map(credit -> credit.toBuilder()
                                        .totalBorrowingCapacity(credit.getMonthlyFee() + creditDTO.getExpenses())
                                        .build()))))
                .filter(userCredit -> userCredit.getTotalBorrowingCapacity() < (accountDTO.getIncome() * 0.4))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.DOES_NOT_MEET_DEBT_CAPACITY))));
    }

    private Double calculateTotalCapacity(CreditDTO creditData, List<CreditDTO> creditDTOS) {
        return creditDTOS.stream()
                .mapToDouble(CreditDTO::getMonthlyFee).sum() + creditData.getExpenses() + creditData.getMonthlyFee();
    }
}
