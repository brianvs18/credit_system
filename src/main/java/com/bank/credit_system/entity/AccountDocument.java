package com.bank.credit_system.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Document
public class AccountDocument {
    @Id
    private String id;
    private String accountType;
    private Long accountNumber;
    private Double availableBalance;
    private Double income;
    private Long creationDate;
    private String userIdentification;
    private String status;
}
