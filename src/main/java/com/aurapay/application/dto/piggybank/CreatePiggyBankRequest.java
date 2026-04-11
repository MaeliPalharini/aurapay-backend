package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePiggyBankRequest {
    private Long customerId;
    private String name;
    private Double targetAmount;
}

