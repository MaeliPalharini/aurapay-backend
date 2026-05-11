package com.aurapay.domain.port.in;

import com.aurapay.application.dto.PixPaymentResponse;

public interface SimulatePixApprovalUseCase {
    PixPaymentResponse execute(Long pixPaymentId);
}
