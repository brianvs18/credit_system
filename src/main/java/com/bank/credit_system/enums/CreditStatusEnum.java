package com.bank.credit_system.enums;

import lombok.Getter;

import java.util.Arrays;
@Getter
public enum CreditStatusEnum {
    ACTIVE("74e7fe11-e19a-41ce-aad2-544c75ee8db1"),
    CANCEL("48614e4b-b6ad-410c-8268-fe3569d5aa66");

    private final String id;

    CreditStatusEnum(String id) {
        this.id = id;
    }

    public static String nameFromId(String id) {
        return Arrays.stream(values())
                .filter(creditStatusEnum -> creditStatusEnum.getId().contentEquals(id))
                .map(Enum::name).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_STATUS_NOT_VALID"));
    }

    public static String idFromName(String name) {
        return Arrays.stream(values())
                .filter(cse -> cse.name().contentEquals(name))
                .map(CreditStatusEnum::getId).findFirst()
                .orElseThrow(() -> new RuntimeException("OBJECT_NAME_NOT_VALID"));
    }
}
