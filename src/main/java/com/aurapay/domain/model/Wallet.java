package com.aurapay.domain.model;
//representando a carteira do cliente no domínio.
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Wallet {

    private Long id;
    private Long customerId;
    private BigDecimal balance;
    private WalletStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
