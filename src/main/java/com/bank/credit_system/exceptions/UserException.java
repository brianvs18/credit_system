package com.bank.credit_system.exceptions;

import com.bank.credit_system.enums.UserErrorEnum;

public class UserException extends RuntimeException{
    public UserException(UserErrorEnum error) {
        super(error.name());
    }
}
