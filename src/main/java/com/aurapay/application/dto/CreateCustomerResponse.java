package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateCustomerResponse {

    private Long customerId;
    private String fullName;
    private String email;
    private String documentNumber;

    private Long walletId;
    private BigDecimal balance;
    private String walletStatus;
}
