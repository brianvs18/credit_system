package com.bank.credit_system.usecase.handler;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.enums.AccountStatusEnum;
import com.bank.credit_system.enums.AccountTypeEnum;
import com.bank.credit_system.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountHandlerUseCase {
    private final AccountRepository accountRepository;

    public Flux<AccountDTO> findAll() {
        return accountRepository.findAll()
                .map(accountDocument -> AccountDTO.builder()
                        .id(accountDocument.getId())
                        .accountType(AccountTypeEnum.nameFromId(accountDocument.getAccountType()))
                        .accountNumber(accountDocument.getAccountNumber())
                        .availableBalance(accountDocument.getAvailableBalance())
                        .creationDate(accountDocument.getCreationDate())
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
                        .creationDate(accountDocument.getCreationDate())
                        .userIdentification(accountDocument.getUserIdentification())
                        .income(accountDocument.getIncome())
                        .status(AccountStatusEnum.nameFromId(accountDocument.getStatus()))
                        .build());
    }
}
