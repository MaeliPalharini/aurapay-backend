package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePiggyBankResponse {
    private Long id;
    private Long customerId;
    private String name;
    private Double targetAmount;
    private Double currentAmount;
    private String status;
    private String createdAt;
}

