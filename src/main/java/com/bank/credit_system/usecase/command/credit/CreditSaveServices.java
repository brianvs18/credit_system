package com.bank.credit_system.usecase.command.credit;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.entity.CreditDocument;
import com.bank.credit_system.enums.CreditErrorEnum;
import com.bank.credit_system.enums.CreditStatusEnum;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.CreditException;
import com.bank.credit_system.exceptions.UserException;
import com.bank.credit_system.repository.CreditRepository;
import com.bank.credit_system.usecase.handler.AccountServices;
import com.bank.credit_system.usecase.handler.CreditServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
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
                        .flatMap(accountDTO -> validateUserIncome(creditDTO, accountDTO))
                        .filter(accountDTO -> Objects.nonNull(creditDTO.getId()))
                        .flatMap(accountDTO -> creditRepository.findById(creditDTO.getId())
                                .map(creditDocument -> creditDocument.toBuilder()
                                        .id(creditDocument.getId())
                                        .creationDate(creditDocument.getCreationDate())
                                        .numberInstallments(creditDocument.getNumberInstallments())
                                        .creditValue(creditDTO.getCreditValue())
                                        .userIdentification(creditDocument.getUserIdentification())
                                        .status(creditDocument.getStatus())
                                        .build()))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(creditDTO)
                                .map(credit -> CreditDocument.builder()
                                        .creditValue(credit.getCreditValue())
                                        .numberInstallments(credit.getNumberInstallments())
                                        .userIdentification(credit.getUserIdentification())
                                        .creationDate(Clock.systemDefaultZone().millis())
                                        .status(CreditStatusEnum.ACTIVE.getId())
                                        .build())
                        )))
                .flatMap(creditRepository::save)
                .thenReturn(creditDTO);
    }

    private Mono<AccountDTO> validateUserHasAccount(CreditDTO creditDTO) {
        return accountServices.findByUserIdentification(creditDTO.getUserIdentification())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.USER_IS_NOT_EXISTS))));
    }

    private Mono<AccountDTO> validateUserIncome(CreditDTO creditDTO, AccountDTO accountDTO) {
        return Mono.just(accountDTO)
                .filter(account -> Objects.nonNull(creditDTO.getExpenses()))
                .flatMap(accountData -> Mono.just(accountData)
                        .filter(account -> accountDTO.getIncome() > creditDTO.getExpenses())
                        .flatMap(userAccount -> validateUserDebtCapacity(creditDTO, accountDTO))
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENDITURES_EXCEED_INCOME)))))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.EXPENSES_ARE_REQUIRED))));
    }

    private Mono<AccountDTO> validateUserDebtCapacity(CreditDTO creditDTO, AccountDTO accountDTO) {
        return Mono.just(accountDTO)
                .filter(userAccount -> (creditDTO.getCreditValue() / creditDTO.getNumberInstallments()) < (accountDTO.getIncome() * 0.4))
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.DOES_NOT_MEET_DEBT_CAPACITY))));
    }
}
