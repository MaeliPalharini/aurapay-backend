package com.aurapay.application.dto;

import com.aurapay.domain.model.PixStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MercadoPagoPixCharge {

    private String mercadoPagoPaymentId;
    private PixStatus status;
    private String qrCode;
    private String qrCodeBase64;
    private String ticketUrl;
}