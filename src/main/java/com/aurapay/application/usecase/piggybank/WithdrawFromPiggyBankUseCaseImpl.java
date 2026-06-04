package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankRequest;
import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.model.piggybank.PiggyBankTransaction;
import com.aurapay.domain.model.piggybank.TransactionType;
import com.aurapay.domain.port.in.piggybank.WithdrawFromPiggyBankUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import com.aurapay.domain.port.out.pyggybank.PiggyBankTransactionRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.port.out.WalletTransactionRepositoryPort;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletTransaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Service
public class WithdrawFromPiggyBankUseCaseImpl implements WithdrawFromPiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;
    private final PiggyBankTransactionRepositoryPort piggyBankTransactionRepository;
    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;

    public WithdrawFromPiggyBankUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository,
                                            PiggyBankTransactionRepositoryPort piggyBankTransactionRepository,
                                            WalletRepositoryPort walletRepository,
                                            WalletTransactionRepositoryPort walletTransactionRepository) {
        this.piggyBankRepository = piggyBankRepository;
        this.piggyBankTransactionRepository = piggyBankTransactionRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    @Transactional
    public WithdrawFromPiggyBankResponse execute(WithdrawFromPiggyBankRequest request) {
        PiggyBank piggyBank = piggyBankRepository.findByIdAndCustomerId(
                request.getPiggyBankId(),
                request.getCustomerId()
        ).orElseThrow(() -> new BusinessException("Cofrinho não encontrado para o cliente informado"));

        LocalDateTime now = LocalDateTime.now();
        piggyBank.withdraw(request.getAmount(), now);

        Long customerId = piggyBank.getCustomerId();
        PiggyBankTransaction transaction = new PiggyBankTransaction(
                piggyBank.getId(),
                customerId,
                TransactionType.WITHDRAW_COFRINHO,
                request.getAmount()
        );

        // Credita o valor resgatado na wallet do cliente
        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o cliente"));

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(request.getAmount())));
        walletRepository.save(wallet);

        piggyBankRepository.save(piggyBank);
        piggyBankTransactionRepository.save(transaction);

        walletTransactionRepository.save(WalletTransaction.piggyBankWithdraw(
                customerId, wallet.getId(), BigDecimal.valueOf(request.getAmount())));

        WithdrawFromPiggyBankResponse response = new WithdrawFromPiggyBankResponse();
        response.setStatus("SUCCESS");
        response.setName(piggyBank.getName());
        response.setCurrentAmount(piggyBank.getCurrentAmount() != null ? piggyBank.getCurrentAmount().doubleValue() : 0.0);
        response.setPiggyBankId(piggyBank.getId());
        response.setPiggyBankStatus(piggyBank.getStatus() != null ? piggyBank.getStatus().name() : null);
        response.setCreatedAt(transaction.getCreatedAt() != null ? transaction.getCreatedAt().toString() : null);
        response.setUpdatedAt(transaction.getUpdatedAt() != null ? transaction.getUpdatedAt().toString() : null);
        response.setTransactionId(transaction.getId());

        return response;
    }
}
