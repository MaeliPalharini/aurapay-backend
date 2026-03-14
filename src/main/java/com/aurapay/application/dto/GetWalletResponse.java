package com.aurapay.application.dto;

import com.aurapay.domain.model.WalletStatus;

import java.math.BigDecimal;

public class GetWalletResponse {

    private Long walletId;
    private Long customerId;
    private BigDecimal balance;
    private WalletStatus walletStatus;

    public GetWalletResponse(Long walletId, Long customerId, BigDecimal balance, WalletStatus walletStatus) {
        this.walletId = walletId;
        this.customerId = customerId;
        this.balance = balance;
        this.walletStatus = walletStatus;
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
}
