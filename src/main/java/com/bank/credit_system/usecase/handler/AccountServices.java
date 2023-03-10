package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.enums.AccountErrorEnum;
import com.bank.credit_system.enums.AccountStatusEnum;
import com.bank.credit_system.enums.AccountTypeEnum;
import com.bank.credit_system.exceptions.AccountErrorException;
import com.bank.credit_system.functions.DateFunction;
import com.bank.credit_system.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountServices {
    private final AccountRepository accountRepository;

    public Flux<AccountDTO> findAll() {
        return accountRepository.findAll()
                .map(accountDocument -> AccountDTO.builder()
                        .id(accountDocument.getId())
                        .accountType(AccountTypeEnum.nameFromId(accountDocument.getAccountType()))
                        .accountNumber(accountDocument.getAccountNumber())
                        .availableBalance(accountDocument.getAvailableBalance())
                        .creationDate(DateFunction.getDateFromMillis(accountDocument.getCreationDate()))
                        .userIdentification(accountDocument.getUserIdentification())
                        .income(accountDocument.getIncome())
                        .status(AccountStatusEnum.nameFromId(accountDocument.getStatus()))
                        .build());
    }

    public Mono<AccountDTO> findById(String accountId) {
        return accountRepository.findById(accountId)
                .map(accountDocument -> AccountDTO.builder()
                        .id(accountDocument.getId())
                        .accountType(AccountTypeEnum.nameFromId(accountDocument.getAccountType()))
                        .accountNumber(accountDocument.getAccountNumber())
                        .availableBalance(accountDocument.getAvailableBalance())
                        .creationDate(DateFunction.getDateFromMillis(accountDocument.getCreationDate()))
                        .userIdentification(accountDocument.getUserIdentification())
                        .income(accountDocument.getIncome())
                        .status(AccountStatusEnum.nameFromId(accountDocument.getStatus()))
                        .build());
    }

    public Mono<AccountDTO> findByUserIdentification(String userIdentification) {
        return accountRepository.findByUserIdentification(userIdentification)
                .map(accountDocument -> AccountDTO.builder()
                        .id(accountDocument.getId())
                        .accountType(AccountTypeEnum.nameFromId(accountDocument.getAccountType()))
                        .accountNumber(accountDocument.getAccountNumber())
                        .availableBalance(accountDocument.getAvailableBalance())
                        .creationDate(DateFunction.getDateFromMillis(accountDocument.getCreationDate()))
                        .userIdentification(accountDocument.getUserIdentification())
                        .income(accountDocument.getIncome())
                        .status(AccountStatusEnum.nameFromId(accountDocument.getStatus()))
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccountErrorException(AccountErrorEnum.USER_HAS_NO_REGISTERED_ACCOUNT))));
    }
}
