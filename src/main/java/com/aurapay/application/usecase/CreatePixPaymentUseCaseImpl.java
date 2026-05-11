package com.aurapay.application.usecase;

import com.aurapay.application.dto.CreatePixPaymentRequest;
import com.aurapay.application.dto.CreatePixPaymentResponse;
import com.aurapay.application.dto.MercadoPagoPixCharge;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.model.PixStatus;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.port.in.CreatePixPaymentUseCase;
import com.aurapay.domain.port.out.MercadoPagoPort;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CreatePixPaymentUseCaseImpl implements CreatePixPaymentUseCase {

    private final WalletRepositoryPort walletRepository;
    private final PixPaymentRepositoryPort pixPaymentRepository;
    private final MercadoPagoPort mercadoPagoPort;

    public CreatePixPaymentUseCaseImpl(
            WalletRepositoryPort walletRepository,
            PixPaymentRepositoryPort pixPaymentRepository,
            MercadoPagoPort mercadoPagoPort
    ) {
        this.walletRepository = walletRepository;
        this.pixPaymentRepository = pixPaymentRepository;
        this.mercadoPagoPort = mercadoPagoPort;
    }

    @Override
    @Transactional
    public CreatePixPaymentResponse execute(CreatePixPaymentRequest request) {
        if (request == null || request.getCustomerId() == null) {
            throw new BusinessException("customerId é obrigatório");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("amount deve ser maior que zero");
        }

        Wallet wallet = walletRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o cliente"));

        PixPayment pixPayment = new PixPayment();
        pixPayment.setCustomerId(request.getCustomerId());
        pixPayment.setWalletId(wallet.getId());
        pixPayment.setAmount(request.getAmount());
        pixPayment.setStatus(PixStatus.PENDING);
        pixPayment.setCreatedAt(LocalDateTime.now());
        pixPayment.setUpdatedAt(LocalDateTime.now());

        MercadoPagoPixCharge charge = mercadoPagoPort.createPixCharge(pixPayment);

        pixPayment.setMercadoPagoPaymentId(charge.getMercadoPagoPaymentId());
        pixPayment.setStatus(charge.getStatus() != null ? charge.getStatus() : PixStatus.PENDING);
        pixPayment.setQrCode(charge.getQrCode());
        pixPayment.setQrCodeBase64(charge.getQrCodeBase64());
        pixPayment.setTicketUrl(charge.getTicketUrl());

        PixPayment saved = pixPaymentRepository.save(pixPayment);

        return new CreatePixPaymentResponse(
                saved.getId(),
                saved.getCustomerId(),
                saved.getAmount(),
                saved.getStatus(),
                saved.getMercadoPagoPaymentId(),
                saved.getQrCode(),
                saved.getQrCodeBase64(),
                saved.getTicketUrl(),
                saved.getCreatedAt()
        );
    }
}