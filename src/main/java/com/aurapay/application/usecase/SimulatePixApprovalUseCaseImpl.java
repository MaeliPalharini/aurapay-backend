package com.aurapay.application.usecase;

import com.aurapay.application.dto.PixPaymentResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.model.PixStatus;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletTransaction;
import com.aurapay.domain.port.in.GetPixPaymentUseCase;
import com.aurapay.domain.port.in.SimulatePixApprovalUseCase;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.port.out.WalletTransactionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Aprovação SIMULADA de um pagamento Pix, para testes.
 *
 * Motivo: o sandbox do Mercado Pago NÃO permite pagar um Pix de verdade
 * (o QR de teste não resolve numa chave real). Então, para demonstrar o ciclo
 * completo (pagar -> creditar carteira), este caso de uso credita a carteira
 * diretamente, sem depender da confirmação real do Mercado Pago.
 *
 * ATENÇÃO: é um recurso de TESTE. Em produção, quem confirma o pagamento é o
 * webhook real do Mercado Pago — este endpoint deve ser desabilitado/removido.
 */
@Service
public class SimulatePixApprovalUseCaseImpl implements SimulatePixApprovalUseCase {

    private static final Logger log = LoggerFactory.getLogger(SimulatePixApprovalUseCaseImpl.class);

    private final PixPaymentRepositoryPort pixPaymentRepository;
    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;
    private final GetPixPaymentUseCase getPixPaymentUseCase;

    public SimulatePixApprovalUseCaseImpl(
            PixPaymentRepositoryPort pixPaymentRepository,
            WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort walletTransactionRepository,
            GetPixPaymentUseCase getPixPaymentUseCase
    ) {
        this.pixPaymentRepository = pixPaymentRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.getPixPaymentUseCase = getPixPaymentUseCase;
    }

    @Override
    @Transactional
    public PixPaymentResponse execute(Long pixPaymentId) {
        PixPayment payment = pixPaymentRepository.findById(pixPaymentId)
                .orElseThrow(() -> new BusinessException("Pagamento Pix não encontrado: " + pixPaymentId));

        // Idempotência: se já foi aprovado, não credita de novo.
        if (payment.getStatus() == PixStatus.APPROVED) {
            log.info("Pagamento Pix {} já estava aprovado; nada a fazer.", pixPaymentId);
            return getPixPaymentUseCase.execute(pixPaymentId);
        }

        log.warn("[TESTE] Aprovando Pix {} manualmente (sandbox não paga Pix real).", pixPaymentId);

        payment.setStatus(PixStatus.APPROVED);
        payment.setUpdatedAt(LocalDateTime.now());

        Wallet wallet = walletRepository.findByCustomerId(payment.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                        "Carteira não encontrada para customerId: " + payment.getCustomerId()));

        wallet.setBalance(wallet.getBalance().add(payment.getAmount()));
        walletRepository.save(wallet);

        walletTransactionRepository.save(WalletTransaction.pixReceived(
                payment.getCustomerId(), wallet.getId(), payment.getAmount()));

        pixPaymentRepository.save(payment);

        log.info("[TESTE] Carteira {} creditada em {} pelo Pix {}.",
                wallet.getId(), payment.getAmount(), pixPaymentId);

        return getPixPaymentUseCase.execute(pixPaymentId);
    }
}