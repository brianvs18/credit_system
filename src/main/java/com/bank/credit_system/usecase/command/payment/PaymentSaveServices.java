package com.bank.credit_system.usecase.command.payment;

import com.bank.credit_system.dto.PaymentDTO;
import com.bank.credit_system.entity.PaymentDocument;
import com.bank.credit_system.enums.PaymentErrorEnum;
import com.bank.credit_system.exceptions.PaymentException;
import com.bank.credit_system.repository.PaymentRepository;
import com.bank.credit_system.usecase.command.credit.CreditSaveServices;
import com.bank.credit_system.usecase.handler.CreditServices;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;

@Service
@RequiredArgsConstructor
public class PaymentSaveServices {
    private final PaymentRepository paymentRepository;
    private final CreditServices creditServices;
    private final CreditSaveServices creditSaveServices;

    public Mono<PaymentDTO> savePayment(PaymentDTO paymentDTO) {
        return Mono.just(paymentDTO)
                .flatMap(paymentData -> creditServices.findByCreditNumber(paymentData.getCreditNumber())
                        .filter(creditDB -> creditDB.getCreditValue() > NumberUtils.DOUBLE_ZERO)
                        .flatMap(creditDB -> Mono.just(creditDB)
                                .filter(creditData -> paymentData.getPayment() <= creditData.getCreditValue()
                                        && paymentData.getPayment() >= creditData.getMonthlyFee())
                                .flatMap(creditDTO -> creditSaveServices.editCreditBeforePayment(creditDTO, paymentDTO.getPayment()))
                                .switchIfEmpty(Mono.defer(() -> Mono.error(new PaymentException(PaymentErrorEnum.PAYMENT_DOES_NOT_MEET_MINIMUM_OR_TOTAL_VALUE)))))
                        .map(creditDTO -> PaymentDocument.builder()
                                .creditNumber(paymentDTO.getCreditNumber())
                                .userIdentification(paymentDTO.getUserIdentification())
                                .payment(paymentDTO.getPayment())
                                .creationDate(Clock.systemDefaultZone().millis())
                                .build())
                        .flatMap(paymentRepository::save)
                        .thenReturn(paymentDTO));
    }
}
