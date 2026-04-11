package com.aurapay.domain.port.in.piggybank;

import com.aurapay.application.dto.piggybank.DepositToPiggyBankRequest;
import com.aurapay.application.dto.piggybank.DepositToPiggyBankResponse;

public interface DepositToPiggyBankUseCase {

   DepositToPiggyBankResponse execute(DepositToPiggyBankRequest request);
}
