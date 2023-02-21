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
public class CreditDocument {
    @Id
    private String id;
    private Double creditValue;
    private Integer numberInstallments;
    private Double monthlyFee;
    private Integer userIdentification;
    private Long creationDate;
    private String status;
}
