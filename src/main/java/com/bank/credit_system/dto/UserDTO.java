package com.bank.credit_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDTO {
    private String id;
    private String name;
    private String lastname;
    private Integer age;
}
