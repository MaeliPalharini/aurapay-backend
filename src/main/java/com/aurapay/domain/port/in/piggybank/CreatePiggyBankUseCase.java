package com.aurapay.domain.port.in.piggybank;

import com.aurapay.application.dto.piggybank.CreatePiggyBankRequest;
import com.aurapay.application.dto.piggybank.CreatePiggyBankResponse;

public interface CreatePiggyBankUseCase {
    CreatePiggyBankResponse execute(CreatePiggyBankRequest request);
}
