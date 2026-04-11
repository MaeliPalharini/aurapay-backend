package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.PiggyBankYieldHistoryResponse;
import com.aurapay.domain.model.piggybank.PiggyBankYield;
import com.aurapay.domain.port.in.piggybank.GetPiggyBankYieldHistoryUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankYieldRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetPiggyBankYieldHistoryUseCaseImpl implements GetPiggyBankYieldHistoryUseCase {

    private final PiggyBankYieldRepositoryPort yieldRepository;

    public GetPiggyBankYieldHistoryUseCaseImpl(PiggyBankYieldRepositoryPort yieldRepository) {
        this.yieldRepository = yieldRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PiggyBankYieldHistoryResponse execute(Long piggyBankId) {
        List<PiggyBankYield> yields = yieldRepository.findAllByPiggyBankIdOrderByYieldDateDesc(piggyBankId);

        PiggyBankYieldHistoryResponse response = new PiggyBankYieldHistoryResponse();
        response.setPiggyBankId(piggyBankId);
        response.setYields(yields.stream().map(y -> {
            PiggyBankYieldHistoryResponse.YieldItem item = new PiggyBankYieldHistoryResponse.YieldItem();
            item.setYieldDate(y.getYieldDate() != null ? y.getYieldDate().toString() : null);
            item.setYieldAmount(y.getYieldAmount() != null ? y.getYieldAmount().doubleValue() : 0.0);
            item.setCreatedAt(y.getCreatedAt() != null ? y.getCreatedAt().toString() : null);
            return item;
        }).collect(Collectors.toList()));

        return response;
    }
}
