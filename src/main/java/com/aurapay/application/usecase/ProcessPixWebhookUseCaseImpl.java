package com.aurapay.application.usecase;

import com.aurapay.application.dto.MercadoPagoPixCharge;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.model.PixStatus;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.port.in.ProcessPixWebhookUseCase;
import com.aurapay.domain.port.out.MercadoPagoPort;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessPixWebhookUseCaseImpl implements ProcessPixWebhookUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcessPixWebhookUseCaseImpl.class);

    private final PixPaymentRepositoryPort pixPaymentRepository;
    private final MercadoPagoPort mercadoPagoPort;
    private final WalletRepositoryPort walletRepository;

    public ProcessPixWebhookUseCaseImpl(
            PixPaymentRepositoryPort pixPaymentRepository,
            MercadoPagoPort mercadoPagoPort,
            WalletRepositoryPort walletRepository
    ) {
        this.pixPaymentRepository = pixPaymentRepository;
        this.mercadoPagoPort = mercadoPagoPort;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public void execute(String mercadoPagoPaymentId) {
        if (mercadoPagoPaymentId == null || mercadoPagoPaymentId.isBlank()) {
            log.warn("Webhook recebido sem mercadoPagoPaymentId; ignorando.");
            return;
        }

        PixPayment payment = pixPaymentRepository.findByMercadoPagoPaymentId(mercadoPagoPaymentId)
                .orElseThrow(() -> new BusinessException(
                        "Pagamento Pix não encontrado para Mercado Pago id: " + mercadoPagoPaymentId
                ));

        if (payment.getStatus() == PixStatus.APPROVED) {
            log.info("Pagamento Pix {} já aprovado anteriormente; nada a fazer.", payment.getId());
            return;
        }

        MercadoPagoPixCharge charge = mercadoPagoPort.fetchPaymentStatus(mercadoPagoPaymentId);
        PixStatus newStatus = charge.getStatus() != null ? charge.getStatus() : PixStatus.PENDING;

        payment.setStatus(newStatus);
        payment.setUpdatedAt(LocalDateTime.now());

        if (newStatus == PixStatus.APPROVED) {
            creditWallet(payment);
        }

        pixPaymentRepository.save(payment);
        log.info("Pagamento Pix {} atualizado para status {}.", payment.getId(), newStatus);
    }

    private void creditWallet(PixPayment payment) {
        Wallet wallet = walletRepository.findByCustomerId(payment.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                        "Carteira não encontrada para customerId: " + payment.getCustomerId()
                ));

        wallet.setBalance(wallet.getBalance().add(payment.getAmount()));
        walletRepository.save(wallet);

        log.info("Wallet {} creditada em {} para pagamento Pix {}.",
                wallet.getId(), payment.getAmount(), payment.getId());
    }
}