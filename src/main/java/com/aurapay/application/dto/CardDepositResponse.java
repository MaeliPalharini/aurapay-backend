package com.aurapay.application.dto;

import com.aurapay.domain.model.PixStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardDepositResponse {

    private PixStatus status;          // APPROVED / REJECTED / PENDING
    private String statusDetail;       // detalhe do MP (ex.: "accredited", "cc_rejected_insufficient_amount")
    private boolean credited;          // se a carteira foi creditada
    private BigDecimal amount;
    private Long customerId;
    private Long walletId;
    private BigDecimal balance;        // saldo atual da carteira
    private String last4;
    private String brand;
    private String mercadoPagoPaymentId;
}