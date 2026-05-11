package com.aurapay.domain.port.in;

import com.aurapay.application.dto.PixKeyResponse;

import java.util.List;

public interface ListPixKeysByCustomerUseCase {
    List<PixKeyResponse> execute(Long customerId);
}