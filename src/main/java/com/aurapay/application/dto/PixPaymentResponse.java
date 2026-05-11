package com.aurapay.application.dto;

import com.aurapay.domain.model.PixStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PixPaymentResponse {

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
}