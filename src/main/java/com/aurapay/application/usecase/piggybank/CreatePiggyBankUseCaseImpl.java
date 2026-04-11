package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.CreatePiggyBankRequest;
import com.aurapay.application.dto.piggybank.CreatePiggyBankResponse;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.model.piggybank.PiggyBankStatus;
import com.aurapay.domain.port.in.piggybank.CreatePiggyBankUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreatePiggyBankUseCaseImpl implements CreatePiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;

    public CreatePiggyBankUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository) {
        this.piggyBankRepository = piggyBankRepository;
    }

    @Override
    @Transactional
    public CreatePiggyBankResponse execute(CreatePiggyBankRequest request) {
        PiggyBank piggyBank = new PiggyBank();
        piggyBank.setCustomerId(request.getCustomerId());
        piggyBank.setName(request.getName());
        piggyBank.setTargetAmount(request.getTargetAmount() != null ? BigDecimal.valueOf(request.getTargetAmount()) : null);
        piggyBank.setCurrentAmount(BigDecimal.ZERO);
        piggyBank.setStatus(PiggyBankStatus.ACTIVE);

        PiggyBank saved = piggyBankRepository.save(piggyBank);

        CreatePiggyBankResponse response = new CreatePiggyBankResponse();
        response.setId(saved.getId());
        response.setCustomerId(saved.getCustomerId());
        response.setName(saved.getName());
        response.setTargetAmount(saved.getTargetAmount() != null ? saved.getTargetAmount().doubleValue() : null);
        response.setCurrentAmount(saved.getCurrentAmount() != null ? saved.getCurrentAmount().doubleValue() : 0.0);
        response.setStatus(saved.getStatus() != null ? saved.getStatus().name() : null);
        response.setCreatedAt(saved.getCreatedAt() != null ? saved.getCreatedAt().toString() : null);

        return response;
    }
}
