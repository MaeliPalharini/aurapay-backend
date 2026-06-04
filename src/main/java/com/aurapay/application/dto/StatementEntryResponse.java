package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Um lançamento do extrato. {@code direction} indica se entrou (CREDIT) ou
 * saiu (DEBIT) da carteira; {@code type} serve para o front escolher o ícone.
 */
@Getter
@Setter
public class StatementEntryResponse {

    private String id;            // chave única para o front (ex.: "PIX-7", "COFRINHO-3")
    private String type;          // PIX_RECEIVED | PIGGY_BANK_DEPOSIT | PIGGY_BANK_WITHDRAW
    private String direction;     // CREDIT | DEBIT
    private String description;   // texto pronto para exibir
    private BigDecimal amount;    // valor sempre positivo; o sinal vem de "direction"
    private LocalDateTime createdAt;

    public StatementEntryResponse(String id, String type, String direction,
                                  String description, BigDecimal amount, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.direction = direction;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
    }
}
