package com.aurapay.domain.port.in;

import com.aurapay.application.dto.CreatePixPaymentRequest;
import com.aurapay.application.dto.CreatePixPaymentResponse;

public interface CreatePixPaymentUseCase {
    CreatePixPaymentResponse execute(CreatePixPaymentRequest request);
}