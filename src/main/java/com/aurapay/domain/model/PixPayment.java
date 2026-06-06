package com.aurapay.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PixPayment {

    private Long id;
    private Long customerId;
    private Long walletId;
    private BigDecimal amount;
    private PixStatus status;
    private String mercadoPagoPaymentId;
    private String qrCode;
    private String qrCodeBase64;
    private String ticketUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Dados do pagador, usados só para montar a cobrança no Mercado Pago (não são persistidos).
    private String payerName;
    private String payerEmail;
}