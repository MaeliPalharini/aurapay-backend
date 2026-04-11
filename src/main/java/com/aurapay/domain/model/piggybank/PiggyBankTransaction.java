package com.aurapay.domain.model.piggybank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "piggy_bank_transactions")

public class PiggyBankTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "piggy_bank_id", nullable = false)
    private Long piggyBankId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PiggyBankTransaction(Long piggyBankId, Long customerId, TransactionType type, Double amount) {
        if (piggyBankId == null) {
            throw new IllegalArgumentException("piggyBankId não pode ser nulo");
        }
        if (customerId == null) {
            throw new IllegalArgumentException("customerId não pode ser nulo");
        }
        if (type == null) {
            throw new IllegalArgumentException("type não pode ser nulo");
        }
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("amount deve ser maior que zero");
        }
        this.piggyBankId = piggyBankId;
        this.customerId = customerId;
        this.type = type;
        this.amount = BigDecimal.valueOf(amount);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    // Mantém compatibilidade com usos antigos (caso algum lugar ainda construa sem customerId)
    public PiggyBankTransaction(Long piggyBankId, TransactionType type, Double amount) {
        this(piggyBankId, 0L, type, amount);
    }

    public boolean isDeposit() {
        return TransactionType.DEPOSIT_COFRINHO.equals(this.type);
    }

    public boolean isWithdraw() {
        return TransactionType.WITHDRAW_COFRINHO.equals(this.type);
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
