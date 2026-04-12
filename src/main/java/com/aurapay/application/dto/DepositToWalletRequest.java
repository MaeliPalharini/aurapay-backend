package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositToWalletRequest {

    private Long customerId;
    private double amount;
}

