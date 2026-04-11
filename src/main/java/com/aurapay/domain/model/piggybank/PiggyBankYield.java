package com.aurapay.domain.model.piggybank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "piggy_bank_yields",
        uniqueConstraints = {
                @UniqueConstraint(name = "ux_piggy_bank_yields_piggy_bank_date", columnNames = {"piggy_bank_id", "yield_date"})
        })
public class PiggyBankYield {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "piggy_bank_id", nullable = false)
    private Long piggyBankId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "yield_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal yieldAmount;

    @Column(name = "yield_date", nullable = false)
    private LocalDate yieldDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
