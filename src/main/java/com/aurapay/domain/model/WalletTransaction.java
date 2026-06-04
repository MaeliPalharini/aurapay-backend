package com.aurapay.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Lançamento no extrato da carteira (ledger). Cada movimentação de dinheiro
 * gera um registro aqui, que é a fonte única do extrato. As fábricas estáticas
 * centralizam descrição e direção (entrada/saída) de cada tipo.
 */
@Getter
@Setter
public class WalletTransaction {

    private Long id;
    private Long customerId;
    private Long walletId;
    private WalletTransactionType type;
    private TransactionDirection direction;
    private BigDecimal amount;
    private String description;
    private String counterpartName; // ex.: nome de quem enviou/recebeu o Pix (futuro)
    private LocalDateTime createdAt;

    public WalletTransaction() {
    }

    private WalletTransaction(Long customerId, Long walletId, WalletTransactionType type,
                              TransactionDirection direction, BigDecimal amount,
                              String description, String counterpartName) {
        this.customerId = customerId;
        this.walletId = walletId;
        this.type = type;
        this.direction = direction;
        this.amount = amount;
        this.description = description;
        this.counterpartName = counterpartName;
        this.createdAt = LocalDateTime.now();
    }

    public static WalletTransaction deposit(Long customerId, Long walletId, BigDecimal amount) {
        return new WalletTransaction(customerId, walletId, WalletTransactionType.DEPOSIT,
                TransactionDirection.CREDIT, amount, "Depósito recebido", null);
    }

    public static WalletTransaction pixReceived(Long customerId, Long walletId, BigDecimal amount) {
        return new WalletTransaction(customerId, walletId, WalletTransactionType.PIX_RECEIVED,
                TransactionDirection.CREDIT, amount, "Pix recebido", null);
    }

    public static WalletTransaction pixSent(Long customerId, Long walletId, BigDecimal amount) {
        return new WalletTransaction(customerId, walletId, WalletTransactionType.PIX_SENT,
                TransactionDirection.DEBIT, amount, "Pix enviado", null);
    }

    public static WalletTransaction piggyBankDeposit(Long customerId, Long walletId, BigDecimal amount) {
        return new WalletTransaction(customerId, walletId, WalletTransactionType.PIGGY_BANK_DEPOSIT,
                TransactionDirection.DEBIT, amount, "Valor enviado para o cofrinho", null);
    }

    public static WalletTransaction piggyBankWithdraw(Long customerId, Long walletId, BigDecimal amount) {
        return new WalletTransaction(customerId, walletId, WalletTransactionType.PIGGY_BANK_WITHDRAW,
                TransactionDirection.CREDIT, amount, "Valor resgatado do cofrinho", null);
    }
}
