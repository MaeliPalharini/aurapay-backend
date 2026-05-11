package com.aurapay.domain.port.in;

public interface ProcessPixWebhookUseCase {
    void execute(String mercadoPagoPaymentId);
}