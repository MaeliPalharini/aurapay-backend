package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CardDepositRequest {

    private Long customerId;
    private BigDecimal amount;
    private String cardToken;       // token gerado pelo SDK do Mercado Pago no front
    private String paymentMethodId; // ex.: "visa", "master" (vem junto do token)
    private Integer installments;   // nº de parcelas (default 1)
}