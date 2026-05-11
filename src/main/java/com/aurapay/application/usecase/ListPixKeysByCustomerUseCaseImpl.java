package com.aurapay.application.usecase;

import com.aurapay.application.dto.PixKeyResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.port.in.ListPixKeysByCustomerUseCase;
import com.aurapay.domain.port.out.PixKeyRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListPixKeysByCustomerUseCaseImpl implements ListPixKeysByCustomerUseCase {

    private final PixKeyRepositoryPort pixKeyRepository;

    public ListPixKeysByCustomerUseCaseImpl(PixKeyRepositoryPort pixKeyRepository) {
        this.pixKeyRepository = pixKeyRepository;
    }

    @Override
    public List<PixKeyResponse> execute(Long customerId) {
        if (customerId == null) {
            throw new BusinessException("customerId é obrigatório");
        }
        return pixKeyRepository.findByCustomerId(customerId).stream()
                .map(k -> new PixKeyResponse(k.getId(), k.getCustomerId(), k.getKeyValue(), k.getCreatedAt()))
                .toList();
    }
}