package com.aurapay.application.usecase;

import com.aurapay.application.dto.SendPixRequest;
import com.aurapay.application.dto.SendPixResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixKey;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletTransaction;
import com.aurapay.domain.port.in.SendPixUseCase;
import com.aurapay.domain.port.out.PixKeyRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.port.out.WalletTransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Envia Pix interno entre carteiras de clientes AuraPay: debita o pagador,
 * credita o destinatário (resolvido pela chave Pix) e registra os dois
 * lançamentos no ledger, que alimentam o extrato dos dois lados.
 */
@Service
public class SendPixUseCaseImpl implements SendPixUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;
    private final PixKeyRepositoryPort pixKeyRepository;

    public SendPixUseCaseImpl(
            WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort walletTransactionRepository,
            PixKeyRepositoryPort pixKeyRepository
    ) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.pixKeyRepository = pixKeyRepository;
    }

    @Override
    @Transactional
    public SendPixResponse execute(SendPixRequest request) {
        if (request == null || request.getCustomerId() == null) {
            throw new BusinessException("customerId é obrigatório");
        }
        if (request.getDestinationPixKey() == null || request.getDestinationPixKey().isBlank()) {
            throw new BusinessException("A chave Pix de destino é obrigatória");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("amount deve ser maior que zero");
        }

        PixKey destinationKey = pixKeyRepository.findByKeyValue(request.getDestinationPixKey().trim())
                .orElseThrow(() -> new BusinessException("Chave Pix de destino não encontrada"));

        Long destinationCustomerId = destinationKey.getCustomerId();
        if (destinationCustomerId.equals(request.getCustomerId())) {
            throw new BusinessException("Não é possível enviar Pix para você mesmo");
        }

        Wallet senderWallet = walletRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o pagador"));

        Wallet destinationWallet = walletRepository.findByCustomerId(destinationCustomerId)
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o destinatário"));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException("Saldo insuficiente para enviar o Pix");
        }

        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        destinationWallet.setBalance(destinationWallet.getBalance().add(request.getAmount()));

        Wallet savedSender = walletRepository.save(senderWallet);
        walletRepository.save(destinationWallet);

        walletTransactionRepository.save(WalletTransaction.pixSent(
                savedSender.getCustomerId(), savedSender.getId(), request.getAmount()));
        walletTransactionRepository.save(WalletTransaction.pixReceived(
                destinationCustomerId, destinationWallet.getId(), request.getAmount()));

        return new SendPixResponse(
                "SUCCESS",
                request.getAmount(),
                savedSender.getBalance(),
                destinationCustomerId,
                request.getDestinationPixKey().trim(),
                LocalDateTime.now()
        );
    }
}