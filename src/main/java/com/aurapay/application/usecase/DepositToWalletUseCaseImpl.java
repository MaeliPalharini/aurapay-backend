package com.aurapay.application.usecase;

import com.aurapay.application.dto.DepositToWalletRequest;
import com.aurapay.application.dto.DepositToWalletResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.port.in.DepositToWalletUseCase;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class DepositToWalletUseCaseImpl implements DepositToWalletUseCase {

    private final WalletRepositoryPort walletRepository;

    public DepositToWalletUseCaseImpl(WalletRepositoryPort walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public DepositToWalletResponse execute(DepositToWalletRequest request) {
        if (request == null || request.getCustomerId() == null) {
            throw new BusinessException("customerId é obrigatório");
        }

        if (request.getAmount() <= 0) {
            throw new BusinessException("Amount must be greater than zero");
        }

        Wallet wallet = walletRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o cliente"));

        wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(request.getAmount())));
        Wallet saved = walletRepository.save(wallet);

        DepositToWalletResponse response = new DepositToWalletResponse();
        response.setStatus("SUCCESS");
        response.setWalletId(saved.getId());
        response.setCustomerId(saved.getCustomerId());
        response.setBalance(saved.getBalance());
        response.setUpdatedAt(saved.getUpdatedAt() != null ? saved.getUpdatedAt().toString() : null);

        return response;
    }
}

