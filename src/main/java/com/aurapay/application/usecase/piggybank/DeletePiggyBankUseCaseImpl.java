package com.aurapay.application.usecase.piggybank;

import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.port.in.piggybank.DeletePiggyBankUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeletePiggyBankUseCaseImpl implements DeletePiggyBankUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;

    public DeletePiggyBankUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository) {
        this.piggyBankRepository = piggyBankRepository;
    }

    @Override
    @Transactional
    public void execute(Long piggyBankId, Long customerId) {
        PiggyBank piggyBank = piggyBankRepository.findByIdAndCustomerId(piggyBankId, customerId)
                .orElseThrow(() -> new BusinessException("Cofrinho não encontrado para o cliente informado"));
        if (piggyBank.getCurrentAmount() != null && piggyBank.getCurrentAmount().doubleValue() > 0) {
            throw new BusinessException("Não é possível excluir cofrinho com saldo maior que zero");
        }
        piggyBankRepository.delete(piggyBank);
    }
}

