package com.bank.credit_system.exceptions;

import com.bank.credit_system.enums.CreditErrorEnum;

public class CreditException extends RuntimeException{
    public CreditException(CreditErrorEnum error) {
        super(error.name());
    }
}
