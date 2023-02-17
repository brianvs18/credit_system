package com.bank.credit_system.exceptions;

import com.bank.credit_system.enums.AccountErrorEnum;

public class AccountErrorException extends RuntimeException{
    public AccountErrorException(AccountErrorEnum error) {
        super(error.name());
    }
}
