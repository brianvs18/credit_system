package com.bank.credit_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class AccountDTO {
    private String id;
    private String accountType;
    private Double accountBalance;
    private Long creationDate;
    private String userId;
}
