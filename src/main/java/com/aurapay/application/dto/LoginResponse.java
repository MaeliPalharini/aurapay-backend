package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Long customerId;
    private String fullName;
    private String email;
}

