package com.aurapay.domain.model.piggybank;

import java.time.LocalDateTime;

public class PiggyBank {
    private Long id;
    private Long customerId;
    private String name;
    private Double targetAmount;
    private Double currentAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
