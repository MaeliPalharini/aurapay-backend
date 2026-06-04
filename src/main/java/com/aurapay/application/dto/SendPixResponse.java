package com.aurapay.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resultado de um envio de Pix interno. {@code senderBalance} já é o saldo
 * do pagador depois do débito, pronto pro front atualizar a tela.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendPixResponse {

    private String status;            // "SUCCESS"
    private BigDecimal amount;        // valor transferido
    private BigDecimal senderBalance; // saldo do pagador após o débito
    private Long destinationCustomerId;
    private String destinationPixKey;
    private LocalDateTime createdAt;
}