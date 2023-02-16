package com.bank.credit_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CreditDTO {
    private String id;
    private Double creditValue;
    private String accountId;
    private Long creationDate;
}
