package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankRequest;
import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankResponse;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.model.piggybank.PiggyBankTransaction;
import com.aurapay.domain.model.piggybank.TransactionType;
import com.aurapay.domain.port.in.piggybank.WithdrawFromPiggyBankUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import com.aurapay.domain.port.out.pyggybank.PiggyBankTransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WithdrawFromPiggyBankUseCaseImpl implements WithdrawFromPiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;
    private final PiggyBankTransactionRepositoryPort piggyBankTransactionRepository;

    public WithdrawFromPiggyBankUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository,
                                            PiggyBankTransactionRepositoryPort piggyBankTransactionRepository) {
        this.piggyBankRepository = piggyBankRepository;
        this.piggyBankTransactionRepository = piggyBankTransactionRepository;
    }

    @Override
    @Transactional
    public WithdrawFromPiggyBankResponse execute(WithdrawFromPiggyBankRequest request) {
        PiggyBank piggyBank = piggyBankRepository.findByIdAndCustomerId(
                request.getPiggyBankId(),
                request.getCustomerId()
        ).orElseThrow(() -> new IllegalArgumentException("Cofrinho não encontrado para o cliente informado"));

        LocalDateTime now = LocalDateTime.now();
        piggyBank.withdraw(request.getAmount(), now);

        Long customerId = piggyBank.getCustomerId();
        PiggyBankTransaction transaction = new PiggyBankTransaction(
                piggyBank.getId(),
                customerId,
                TransactionType.WITHDRAW_COFRINHO,
                request.getAmount()
        );

        piggyBankRepository.save(piggyBank);
        piggyBankTransactionRepository.save(transaction);

        WithdrawFromPiggyBankResponse response = new WithdrawFromPiggyBankResponse();
        response.setStatus("SUCCESS");
        response.setName(piggyBank.getName());
        response.setCurrentAmount(piggyBank.getCurrentAmount() != null ? piggyBank.getCurrentAmount().doubleValue() : 0.0);
        response.setPiggyBankId(piggyBank.getId());
        response.setPiggyBankStatus(piggyBank.getStatus() != null ? piggyBank.getStatus().name() : null);
        response.setCreatedAt(transaction.getCreatedAt() != null ? transaction.getCreatedAt().toString() : null);
        response.setUpdatedAt(transaction.getUpdatedAt() != null ? transaction.getUpdatedAt().toString() : null);
        response.setTransactionId(transaction.getId());

        return response;
    }
}
