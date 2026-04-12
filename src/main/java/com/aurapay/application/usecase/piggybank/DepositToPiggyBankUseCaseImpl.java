package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.DepositToPiggyBankRequest;
import com.aurapay.application.dto.piggybank.DepositToPiggyBankResponse;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.model.piggybank.PiggyBankTransaction;
import com.aurapay.domain.model.piggybank.TransactionType;
import com.aurapay.domain.port.in.piggybank.DepositToPiggyBankUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import com.aurapay.domain.port.out.pyggybank.PiggyBankTransactionRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.model.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DepositToPiggyBankUseCaseImpl implements DepositToPiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;
    private final PiggyBankTransactionRepositoryPort piggyBankTransactionRepository;
    private final WalletRepositoryPort walletRepository;

    public DepositToPiggyBankUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository,
                                         PiggyBankTransactionRepositoryPort piggyBankTransactionRepository,
                                         WalletRepositoryPort walletRepository) {
        this.piggyBankRepository = piggyBankRepository;
        this.piggyBankTransactionRepository = piggyBankTransactionRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public DepositToPiggyBankResponse execute(DepositToPiggyBankRequest request) {
        PiggyBank piggyBank = piggyBankRepository.findByIdAndCustomerId(
                request.getPiggyBankId(),
                request.getCustomerId() != null ? Long.valueOf(request.getCustomerId()) : null
        ).orElseThrow(() -> new IllegalArgumentException("Cofrinho não encontrado para o cliente informado"));

        Long customerId = piggyBank.getCustomerId();

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Carteira não encontrada para o cliente"));

        if (wallet.getBalance().compareTo(java.math.BigDecimal.valueOf(request.getAmount())) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na carteira para realizar o depósito");
        }

        wallet.setBalance(wallet.getBalance().subtract(java.math.BigDecimal.valueOf(request.getAmount())));
        walletRepository.save(wallet);

        LocalDateTime now = LocalDateTime.now();
        piggyBank.deposit(request.getAmount(), now);

        PiggyBankTransaction transaction = new PiggyBankTransaction(
                piggyBank.getId(),
                customerId,
                TransactionType.DEPOSIT_COFRINHO,
                request.getAmount()
        );

        piggyBankRepository.save(piggyBank);
        piggyBankTransactionRepository.save(transaction);

        DepositToPiggyBankResponse response = new DepositToPiggyBankResponse();
        response.setStatus("SUCCESS");
        response.setName(piggyBank.getName());
        response.setCurrentAmount(piggyBank.getCurrentAmount() != null ? piggyBank.getCurrentAmount().doubleValue() : 0.0);
        response.setPiggyBankId(String.valueOf(piggyBank.getId()));
        response.setPiggyBankStatus(piggyBank.getStatus() != null ? piggyBank.getStatus().name() : null);
        response.setCreatedAt(transaction.getCreatedAt() != null ? transaction.getCreatedAt().toString() : null);
        response.setUpdatedAt(transaction.getUpdatedAt() != null ? transaction.getUpdatedAt().toString() : null);
        response.setTransactionId(transaction.getId() != null ? String.valueOf(transaction.getId()) : null);

        return response;
    }
}
