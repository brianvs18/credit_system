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
public class PaymentDocument {
    @Id
    private String id;
    private Integer creditNumber;
    private String userIdentification;
    private Double payment;
    private Long creationDate;
}
