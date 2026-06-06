package com.aurapay.application.dto;

import com.aurapay.domain.model.PixStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Resultado de um pagamento com cartão no Mercado Pago.
 * Reutiliza {@link PixStatus} como status genérico de pagamento (approved/rejected/pending).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoCardCharge {

    private String mercadoPagoPaymentId;
    private PixStatus status;
    private String statusDetail; // ex.: "accredited", "cc_rejected_insufficient_amount"
    private String last4;        // 4 últimos dígitos do cartão (vem do MP)
    private String brand;        // bandeira / payment_method_id (ex.: "visa", "master")
}