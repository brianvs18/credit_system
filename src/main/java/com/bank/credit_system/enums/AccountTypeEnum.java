package com.bank.credit_system.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum AccountTypeEnum {
    SAVINGS("a3d003e4-1c06-43c2-a933-e2dbe1623252"),
    CURRENT("86ce53c5-bc55-48b9-aba1-3281ab7534b6");

    private final String id;

    AccountTypeEnum(String id) {
        this.id = id;
    }

    public static String nameFromId(String id) {
        return Arrays.stream(values())
                .filter(accountTypeEnum -> accountTypeEnum.getId().contentEquals(id))
                .map(Enum::name).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_STATUS_NOT_VALID"));
    }

    public static String idFromName(String name) {
        return Arrays.stream(values())
                .filter(dse -> dse.name().contentEquals(name))
                .map(AccountTypeEnum::getId).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_NAME_NOT_VALID"));
    }
}
