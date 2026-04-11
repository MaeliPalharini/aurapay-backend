package com.aurapay.application.dto.piggybank;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PiggyBankYieldHistoryResponse {

    private Long piggyBankId;
    private List<YieldItem> yields;

    @Getter
    @Setter
    public static class YieldItem {
        private String yieldDate;
        private Double yieldAmount;
        private String createdAt;
    }
}

