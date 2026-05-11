package com.aurapay.application.usecase;

import com.aurapay.application.dto.CreatePixKeyRequest;
import com.aurapay.application.dto.PixKeyResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixKey;
import com.aurapay.domain.port.in.CreatePixKeyUseCase;
import com.aurapay.domain.port.out.PixKeyRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CreatePixKeyUseCaseImpl implements CreatePixKeyUseCase {

    private static final int MAX_KEYS_PER_CUSTOMER = 5;

    private final PixKeyRepositoryPort pixKeyRepository;
    private final WalletRepositoryPort walletRepository;

    public CreatePixKeyUseCaseImpl(
            PixKeyRepositoryPort pixKeyRepository,
            WalletRepositoryPort walletRepository
    ) {
        this.pixKeyRepository = pixKeyRepository;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public PixKeyResponse execute(CreatePixKeyRequest request) {
        if (request == null || request.getCustomerId() == null) {
            throw new BusinessException("customerId é obrigatório");
        }

        walletRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new BusinessException(
                        "Cliente não encontrado: " + request.getCustomerId()
                ));

        int existing = pixKeyRepository.findByCustomerId(request.getCustomerId()).size();
        if (existing >= MAX_KEYS_PER_CUSTOMER) {
            throw new BusinessException(
                    "Limite de " + MAX_KEYS_PER_CUSTOMER + " chaves Pix por cliente atingido"
            );
        }

        PixKey pixKey = new PixKey();
        pixKey.setCustomerId(request.getCustomerId());
        pixKey.setKeyValue(UUID.randomUUID().toString());
        pixKey.setCreatedAt(LocalDateTime.now());

        PixKey saved = pixKeyRepository.save(pixKey);

        return new PixKeyResponse(
                saved.getId(),
                saved.getCustomerId(),
                saved.getKeyValue(),
                saved.getCreatedAt()
        );
    }
}