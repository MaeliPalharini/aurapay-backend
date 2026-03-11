package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerRequest {

    private String fullName;
    private String email;
    private String documentNumber;
}