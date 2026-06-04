package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Pedido de envio de Pix interno (entre carteiras de clientes AuraPay).
 * {@code customerId} é o pagador (sai da sessão autenticada no front) e
 * {@code destinationPixKey} é a chave Pix de quem vai receber.
 */
@Getter
@Setter
public class SendPixRequest {

    private Long customerId;
    private String destinationPixKey;
    private BigDecimal amount;
}