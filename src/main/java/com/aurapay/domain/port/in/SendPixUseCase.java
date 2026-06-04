package com.aurapay.domain.port.in;

import com.aurapay.application.dto.SendPixRequest;
import com.aurapay.application.dto.SendPixResponse;

public interface SendPixUseCase {
    SendPixResponse execute(SendPixRequest request);
}