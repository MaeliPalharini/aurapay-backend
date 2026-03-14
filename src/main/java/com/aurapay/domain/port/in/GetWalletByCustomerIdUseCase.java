package com.aurapay.domain.port.in;

import com.aurapay.application.dto.GetWalletResponse;

public interface GetWalletByCustomerIdUseCase {
    GetWalletResponse execute(Long customerId);
}
