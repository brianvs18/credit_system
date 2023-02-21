package com.bank.credit_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ErrorDTO {
    private String requestId;
    private String message;
    private String status;
}
