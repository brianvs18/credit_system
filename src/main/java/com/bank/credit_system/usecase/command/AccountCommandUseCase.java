package com.bank.credit_system.usecase.command;

import com.bank.credit_system.dto.AccountDTO;
import com.bank.credit_system.entity.AccountDocument;
import com.bank.credit_system.enums.AccountStatusEnum;
import com.bank.credit_system.enums.AccountTypeEnum;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.UserException;
import com.bank.credit_system.repository.AccountRepository;
import com.bank.credit_system.usecase.handler.UserHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AccountCommandUseCase {
    private final AccountRepository accountRepository;
    private final UserHandlerUseCase userHandlerUseCase;

    public Mono<AccountDTO> saveAccount(AccountDTO accountDTO) {
        return Mono.just(accountDTO)
                .flatMap(accountData -> userHandlerUseCase.findById(accountDTO.getUserId())
                        .switchIfEmpty(Mono.defer(()-> Mono.error(new UserException(UserErrorEnum.USER_IS_NOT_EXISTS))))
                        .filter(userDTO -> Objects.nonNull(accountDTO.getId()))
                        .flatMap(userDTO -> accountRepository.findById(accountDTO.getId())
                                .map(accountDB -> AccountDocument.builder()
                                        .id(accountDB.getId())
                                        .accountType(AccountTypeEnum.idFromName(accountDTO.getAccountType()))
                                        .income(accountDTO.getIncome())
                                        .userId(accountDTO.getUserId())
                                        .availableBalance(accountDB.getAvailableBalance())
                                        .creationDate(accountDB.getCreationDate())
                                        .status(accountDB.getStatus())
                                        .build()))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(accountDTO)
                                .map(account -> AccountDocument.builder()
                                        .id(account.getId())
                                        .accountType(AccountTypeEnum.idFromName(account.getAccountType()))
                                        .income(account.getIncome())
                                        .userId(account.getUserId())
                                        .creationDate(Clock.systemDefaultZone().millis())
                                        .status(AccountStatusEnum.ACTIVE.getId())
                                        .build())
                        )))
                .flatMap(accountRepository::save)
                .thenReturn(accountDTO);
    }

    public Mono<Void> deleteAccount(String accountId){
        return accountRepository.deleteById(accountId);
    }
}
