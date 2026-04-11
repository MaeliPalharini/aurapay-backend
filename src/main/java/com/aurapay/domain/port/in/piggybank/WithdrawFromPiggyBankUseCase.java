package com.aurapay.domain.port.in.piggybank;

import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankRequest;
import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankResponse;

public interface WithdrawFromPiggyBankUseCase {

    WithdrawFromPiggyBankResponse execute(WithdrawFromPiggyBankRequest request);
}
