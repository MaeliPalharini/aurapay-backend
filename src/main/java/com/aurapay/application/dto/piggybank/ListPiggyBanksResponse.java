package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListPiggyBanksResponse {
    private List<PiggyBankSummary> piggyBanks;

    @Getter
    @Setter
    public static class PiggyBankSummary {
        private Long id;
        private String name;
        private Double targetAmount;
        private Double currentAmount;
        private String status;
        private String createdAt;
    }
}

