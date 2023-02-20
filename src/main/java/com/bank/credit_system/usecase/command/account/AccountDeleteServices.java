package com.bank.credit_system.usecase.command.account;

import com.bank.credit_system.enums.AccountStatusEnum;
import com.bank.credit_system.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountDeleteServices {
    private final AccountRepository accountRepository;

    public Mono<Void> deleteAccount(String accountId) {
        return accountRepository.findById(accountId)
                .map(accountDocument -> accountDocument.toBuilder()
                        .status(AccountStatusEnum.DISABLE.getId())
                        .build())
                .then();
    }
}
