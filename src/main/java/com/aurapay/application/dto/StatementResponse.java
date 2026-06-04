package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class StatementResponse {

    private BigDecimal saldoAtual;
    private List<StatementEntryResponse> transactions;

    public StatementResponse(BigDecimal saldoAtual, List<StatementEntryResponse> transactions) {
        this.saldoAtual = saldoAtual;
        this.transactions = transactions;
    }
}
