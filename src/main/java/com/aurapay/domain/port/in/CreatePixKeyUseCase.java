package com.aurapay.domain.port.in;

import com.aurapay.application.dto.CreatePixKeyRequest;
import com.aurapay.application.dto.PixKeyResponse;

public interface CreatePixKeyUseCase {
    PixKeyResponse execute(CreatePixKeyRequest request);
}