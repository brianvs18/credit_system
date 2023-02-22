package com.bank.credit_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {
    private String id;
    private String accountType;
    private Long accountNumber;
    private Double availableBalance;
    private Double income;
    private String creationDate;
    private Integer userIdentification;
    private String status;
}
