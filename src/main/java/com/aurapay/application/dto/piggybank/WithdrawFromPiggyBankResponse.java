package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawFromPiggyBankResponse {
    private String status;
    private String name;
    private Double currentAmount;
    private Long piggyBankId;
    private String piggyBankStatus;
    private String createdAt;
    private String updatedAt;
    private Long transactionId;
}
