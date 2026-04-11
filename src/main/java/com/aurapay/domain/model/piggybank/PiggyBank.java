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
@Table(name = "piggy_banks")
//Representa o cofrinho do cliente,
//contendo informações como o valor atual, o valor alvo e as transações associadas.
public class PiggyBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "target_amount", precision = 19, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "current_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal currentAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PiggyBankStatus status = PiggyBankStatus.ACTIVE;

    @Column(name = "transaction_count", nullable = false)
    private Long transactionCount = 0L;

    @Column(name = "last_transaction_at")
    private LocalDateTime lastTransactionAt;

    @Column(name = "total_yield_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalYieldAmount = BigDecimal.ZERO;

    @Column(name = "last_yield_date")
    private LocalDateTime lastYieldDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // --- Regras de negócio ---
    public boolean isActive() {
        return PiggyBankStatus.ACTIVE.equals(this.status);
    }

    public void deposit(Double amount, LocalDateTime now) {
        validateActive();
        validatePositiveAmount(amount);
        if (this.currentAmount == null) {
            this.currentAmount = BigDecimal.ZERO;
        }
        this.currentAmount = this.currentAmount.add(BigDecimal.valueOf(amount));
        touchTransactionMetadata(now);
    }

    public void withdraw(Double amount, LocalDateTime now) {
        validateActive();
        validatePositiveAmount(amount);
        if (this.currentAmount == null) {
            this.currentAmount = BigDecimal.ZERO;
        }
        BigDecimal bdAmount = BigDecimal.valueOf(amount);
        if (this.currentAmount.compareTo(bdAmount) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente no cofrinho");
        }
        this.currentAmount = this.currentAmount.subtract(bdAmount);
        touchTransactionMetadata(now);
    }

    public boolean canWithdraw(Double amount) {
        if (!isActive() || amount == null || amount <= 0) {
            return false;
        }
        if (currentAmount == null) {
            return false;
        }
        return currentAmount.compareTo(BigDecimal.valueOf(amount)) >= 0;
    }

    public void applyYield(Double yieldAmount, LocalDateTime date) {
        if (yieldAmount == null || yieldAmount <= 0) {
            return;
        }
        if (this.currentAmount == null) {
            this.currentAmount = BigDecimal.ZERO;
        }
        if (this.totalYieldAmount == null) {
            this.totalYieldAmount = BigDecimal.ZERO;
        }
        BigDecimal bdYield = BigDecimal.valueOf(yieldAmount);
        this.currentAmount = this.currentAmount.add(bdYield);
        this.totalYieldAmount = this.totalYieldAmount.add(bdYield);
        this.lastYieldDate = date != null ? date : LocalDateTime.now();
        this.updatedAt = this.lastYieldDate;
    }

    public void close() {
        if (this.currentAmount != null && this.currentAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("Não é possível fechar cofrinho com saldo maior que zero");
        }
        this.status = PiggyBankStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    private void validateActive() {
        if (!isActive()) {
            throw new IllegalStateException("Operação não permitida: cofrinho não está ativo");
        }
    }

    private void validatePositiveAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Valor da operação deve ser positivo");
        }
    }

    private void touchTransactionMetadata(LocalDateTime now) {
        this.lastTransactionAt = now != null ? now : LocalDateTime.now();
        if (this.transactionCount == null) {
            this.transactionCount = 0L;
        }
        this.transactionCount++;
        this.updatedAt = this.lastTransactionAt;
    }

    @PrePersist
    void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        if (updatedAt == null) updatedAt = now;
        if (currentAmount == null) currentAmount = BigDecimal.ZERO;
        if (totalYieldAmount == null) totalYieldAmount = BigDecimal.ZERO;
        if (transactionCount == null) transactionCount = 0L;
        if (status == null) status = PiggyBankStatus.ACTIVE;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
