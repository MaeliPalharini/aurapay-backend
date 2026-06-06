package com.aurapay.domain.port.in;

import com.aurapay.application.dto.CardDepositRequest;
import com.aurapay.application.dto.CardDepositResponse;

public interface CreateCardDepositUseCase {
    CardDepositResponse execute(CardDepositRequest request);
}