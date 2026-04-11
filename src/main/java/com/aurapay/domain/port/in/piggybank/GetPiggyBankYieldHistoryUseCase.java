package com.aurapay.domain.port.in.piggybank;

import com.aurapay.application.dto.piggybank.PiggyBankYieldHistoryResponse;

public interface GetPiggyBankYieldHistoryUseCase {

    PiggyBankYieldHistoryResponse execute(Long piggyBankId);
}

