package com.bank.credit_system.usecase.command;

import com.bank.credit_system.dto.CreditDTO;
import com.bank.credit_system.entity.CreditDocument;
import com.bank.credit_system.enums.UserErrorEnum;
import com.bank.credit_system.exceptions.UserException;
import com.bank.credit_system.repository.CreditRepository;
import com.bank.credit_system.usecase.handler.AccountHandlerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreditCommandUseCase {
    private final CreditRepository creditRepository;
    private final AccountHandlerUseCase accountHandlerUseCase;

    public Mono<CreditDTO> saveCredit(CreditDTO creditDTO) {
        return Mono.just(creditDTO)
                .flatMap(creditData -> accountHandlerUseCase.findByUserIdentification(creditDTO.getUserIdentification())
                        .switchIfEmpty(Mono.defer(() -> Mono.error(new UserException(UserErrorEnum.USER_IS_NOT_EXISTS))))
                        .filter(accountDTO -> Objects.nonNull(creditDTO.getId()))
                        .flatMap(accountDTO -> creditRepository.findById(creditDTO.getId())
                                .map(creditDocument -> creditDocument.toBuilder()
                                        .id(creditDocument.getId())
                                        .creationDate(creditDocument.getCreationDate())
                                        .creditValue(creditDTO.getCreditValue())
                                        .expenses(creditDTO.getExpenses())
                                        .userIdentification(creditDocument.getUserIdentification())
                                        .build()))
                        .switchIfEmpty(Mono.defer(() -> Mono.just(creditDTO)
                                .map(credit -> CreditDocument.builder()
                                        .creditValue(credit.getCreditValue())
                                        .userIdentification(credit.getUserIdentification())
                                        .expenses(credit.getExpenses())
                                        .creationDate(Clock.systemDefaultZone().millis())
                                        .build())
                        )))
                .flatMap(creditRepository::save)
                .thenReturn(creditDTO);
    }
}
