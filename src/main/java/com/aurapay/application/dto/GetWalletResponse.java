package com.aurapay.application.dto;

import com.aurapay.domain.model.WalletStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class GetWalletResponse {

    private Long walletId;
    private Long customerId;
    private BigDecimal balance;
    private WalletStatus walletStatus;
    private LocalDateTime updatedAt;

    public GetWalletResponse(
            Long walletId,
            Long customerId,
            BigDecimal balance,
            WalletStatus walletStatus,
            LocalDateTime updatedAt
    ) {
        this.walletId = walletId;
        this.customerId = customerId;
        this.balance = balance;
        this.walletStatus = walletStatus;
        this.updatedAt = updatedAt;
    }

    public Long getWalletId() {
        return walletId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getWalletStatus() {
        return walletStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}