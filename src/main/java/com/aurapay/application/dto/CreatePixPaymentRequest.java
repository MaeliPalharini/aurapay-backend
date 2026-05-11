package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreatePixPaymentRequest {

    private Long customerId;
    private BigDecimal amount;
    private String payerEmail;
}