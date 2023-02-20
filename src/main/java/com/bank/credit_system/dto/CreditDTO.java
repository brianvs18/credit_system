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
    private Integer numberInstallments;
    private Integer userIdentification;
    private Double expenses;
    private Long creationDate;
    private String status;
}
