package com.aurapay.domain.model;

public enum WalletTransactionType {
    DEPOSIT,               // depósito direto na carteira
    PIX_RECEIVED,          // Pix recebido (cobrança aprovada)
    PIX_SENT,              // Pix enviado para alguém (funcionalidade futura)
    PIGGY_BANK_DEPOSIT,    // valor enviado para o cofrinho
    PIGGY_BANK_WITHDRAW,   // valor resgatado do cofrinho
    CARD_DEPOSIT           // recarga da carteira via cartão (Mercado Pago)
}
