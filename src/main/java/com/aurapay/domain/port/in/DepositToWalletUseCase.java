package com.aurapay.domain.port.in;

import com.aurapay.application.dto.DepositToWalletRequest;
import com.aurapay.application.dto.DepositToWalletResponse;

public interface DepositToWalletUseCase {
    DepositToWalletResponse execute(DepositToWalletRequest request);
}

