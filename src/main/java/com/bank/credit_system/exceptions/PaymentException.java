package com.bank.credit_system.exceptions;

import com.bank.credit_system.enums.PaymentErrorEnum;

public class PaymentException extends RuntimeException{
    public PaymentException(PaymentErrorEnum error) {
        super(error.name());
    }
}
