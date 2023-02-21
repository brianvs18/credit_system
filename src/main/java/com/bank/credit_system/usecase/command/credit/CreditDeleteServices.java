package com.bank.credit_system.usecase.command.credit;

import com.bank.credit_system.enums.CreditErrorEnum;
import com.bank.credit_system.enums.CreditStatusEnum;
import com.bank.credit_system.exceptions.CreditException;
import com.bank.credit_system.repository.CreditRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreditDeleteServices {
    private final CreditRepository creditRepository;

    public Mono<Void> deleteCredit(String creditId) {
        return creditRepository.findById(creditId)
                .filter(creditDocument -> Objects.equals(creditDocument.getCreditValue(), NumberUtils.DOUBLE_ZERO))
                .map(creditDocument -> creditDocument.toBuilder()
                        .status(CreditStatusEnum.CANCEL.getId())
                        .build())
                .switchIfEmpty(Mono.defer(() -> Mono.error(new CreditException(CreditErrorEnum.USER_HAS_DEBT_BALANCE))))
                .then();
    }
}
