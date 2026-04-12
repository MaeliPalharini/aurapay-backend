package com.aurapay.domain.port.in.piggybank;

public interface DeletePiggyBankUseCase {
    void execute(Long piggyBankId, Long customerId);
}

