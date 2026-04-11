package com.aurapay.domain.port.in.piggybank;

import com.aurapay.application.dto.piggybank.ListPiggyBanksResponse;

public interface ListPiggyBanksUseCase {

    ListPiggyBanksResponse execute(Long customerId);
}
