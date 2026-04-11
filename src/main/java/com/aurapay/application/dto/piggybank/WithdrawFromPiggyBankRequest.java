package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawFromPiggyBankRequest {
    private Long customerId;
    private Long piggyBankId;
    private Double amount;
}
