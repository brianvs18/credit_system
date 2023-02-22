package com.bank.credit_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreditDTO {
    private String id;
    private Double creditValue;
    private Integer creditNumber;
    private Integer numberInstallments;
    private Integer userIdentification;
    private Double expenses;
    private Double monthlyFee;
    private Double totalBorrowingCapacity;
    private String creationDate;
    private String status;
}
