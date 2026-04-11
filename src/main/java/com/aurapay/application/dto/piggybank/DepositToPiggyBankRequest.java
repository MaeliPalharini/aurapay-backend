package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositToPiggyBankRequest {

    private Long piggyBankId;
    private double amount;
    private String customerId;
}
