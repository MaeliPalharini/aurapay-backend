package com.aurapay.application.usecase;

import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixKey;
import com.aurapay.domain.port.in.DeletePixKeyUseCase;
import com.aurapay.domain.port.out.PixKeyRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeletePixKeyUseCaseImpl implements DeletePixKeyUseCase {

    private final PixKeyRepositoryPort pixKeyRepository;

    public DeletePixKeyUseCaseImpl(PixKeyRepositoryPort pixKeyRepository) {
        this.pixKeyRepository = pixKeyRepository;
    }

    @Override
    @Transactional
    public void execute(Long pixKeyId, Long customerId) {
        if (pixKeyId == null || customerId == null) {
            throw new BusinessException("pixKeyId e customerId são obrigatórios");
        }

        PixKey pixKey = pixKeyRepository.findById(pixKeyId)
                .orElseThrow(() -> new BusinessException("Chave Pix não encontrada"));

        if (!pixKey.getCustomerId().equals(customerId)) {
            throw new BusinessException("Chave Pix não pertence ao cliente informado");
        }

        pixKeyRepository.delete(pixKey);
    }
}