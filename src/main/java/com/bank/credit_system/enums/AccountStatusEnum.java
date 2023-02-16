package com.bank.credit_system.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum AccountStatusEnum {
    ACTIVE("74e7fe11-e19a-41ce-aad2-544c75ee8db1"),
    DISABLE("81b34661-2d51-4335-a4c6-5d47d465a40c");

    private final String id;

    AccountStatusEnum(String id) {
        this.id = id;
    }

    public static String nameFromId(String id) {
        return Arrays.stream(values())
                .filter(accountStatusEnum -> accountStatusEnum.getId().contentEquals(id))
                .map(Enum::name).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_STATUS_NOT_VALID"));
    }

    public static String idFromName(String name) {
        return Arrays.stream(values())
                .filter(dse -> dse.name().contentEquals(name))
                .map(AccountStatusEnum::getId).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_NAME_NOT_VALID"));
    }
}
