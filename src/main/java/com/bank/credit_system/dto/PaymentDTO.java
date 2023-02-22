package com.bank.credit_system.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDTO {
    private String id;
    private Integer creditNumber;
    private Integer userIdentification;
    private Double payment;
    private String creationDate;
}
