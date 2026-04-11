package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DepositToPiggyBankResponse {
    private String status;
    private String name;
    private Double currentAmount;
    private String piggyBankId;
    private String piggyBankStatus;
    private String createdAt;
    private String updatedAt;
    private String transactionId;
}
