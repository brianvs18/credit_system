package com.bank.credit_system.usecase.command;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.entity.AccountDocument;
import com.bank.credit_system.enums.AccountErrorEnum;
import com.bank.credit_system.enums.AccountStatusEnum;
import com.bank.credit_system.enums.AccountTypeEnum;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.AccountErrorException;
import com.bank.credit_system.exceptions.UserException;
import com.bank.credit_system.repository.AccountRepository;
import com.bank.credit_system.usecase.handler.UserHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountCommandUseCase {
    private final AccountRepository accountRepository;
    private final UserHandlerUseCase userHandlerUseCase;
    private static final Long MIN_VALUE = 11111111111L;
    private static final Long MAX_VALUE = 99999999999L;

    public Mono<AccountDTO> saveAccount(AccountDTO accountDTO) {
        return Mono.just(accountDTO)
                .flatMap(accountData -> userHandlerUseCase.findByIdentification(accountDTO.getUserIdentification())
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.USER_IS_NOT_EXISTS))))
                        .filter(userDTO -> Objects.nonNull(accountDTO.getId()))
                        .flatMap(userDTO -> accountRepository.findById(accountDTO.getId())
                                .filter(accountDocument -> Objects.equals(accountDocument.getAccountNumber(), accountDTO.getAccountNumber()))
                                .map(accountDB -> accountDB.toBuilder()
                                        .id(accountDB.getId())
                                        .accountType(AccountTypeEnum.idFromName(accountDTO.getAccountType()))
                                        .accountNumber(accountDB.getAccountNumber())
                                        .income(accountDTO.getIncome())
                                        .userIdentification(accountDB.getUserIdentification())
                                        .availableBalance(accountDB.getAvailableBalance())
                                        .creationDate(accountDB.getCreationDate())
                                        .status(accountDB.getStatus())
                                        .build())
                                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccountErrorException(AccountErrorEnum.EDITING_ACCOUNT_NUMBER_NOT_ALLOWED)))))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(accountDTO)
                                .map(account -> AccountDocument.builder()
                                        .accountType(AccountTypeEnum.idFromName(account.getAccountType()))
                                        .accountNumber(RandomUtils.nextLong(MIN_VALUE, MAX_VALUE))
                                        .income(account.getIncome())
                                        .userIdentification(account.getUserIdentification())
                                        .creationDate(Clock.systemDefaultZone().millis())
                                        .status(AccountStatusEnum.ACTIVE.getId())
                                        .build())
                        )))
                .flatMap(accountRepository::save)
                .thenReturn(accountDTO);
    }

    public Mono<Void> deleteAccount(String accountId) {
        return accountRepository.findById(accountId)
                .map(accountDocument -> accountDocument.toBuilder()
                        .status(AccountStatusEnum.DISABLE.getId())
                        .build())
                .then();
    }
}
