package com.aurapay.application.usecase.piggybank;

import com.aurapay.application.dto.piggybank.ListPiggyBanksResponse;
import com.aurapay.domain.model.piggybank.PiggyBank;
import com.aurapay.domain.port.in.piggybank.ListPiggyBanksUseCase;
import com.aurapay.domain.port.out.pyggybank.PiggyBankRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListPiggyBanksUseCaseImpl implements ListPiggyBanksUseCase {

    private final PiggyBankRepositoryPort piggyBankRepository;

    public ListPiggyBanksUseCaseImpl(PiggyBankRepositoryPort piggyBankRepository) {
        this.piggyBankRepository = piggyBankRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ListPiggyBanksResponse execute(Long customerId) {
        List<PiggyBank> piggyBanks = piggyBankRepository.findAllByCustomerId(customerId);

        ListPiggyBanksResponse response = new ListPiggyBanksResponse();
        response.setPiggyBanks(
                piggyBanks.stream()
                        .map(this::toSummary)
                        .collect(Collectors.toList())
        );
        return response;
    }

    private ListPiggyBanksResponse.PiggyBankSummary toSummary(PiggyBank pb) {
        ListPiggyBanksResponse.PiggyBankSummary s = new ListPiggyBanksResponse.PiggyBankSummary();
        s.setId(pb.getId());
        s.setName(pb.getName());
        s.setTargetAmount(pb.getTargetAmount() != null ? pb.getTargetAmount().doubleValue() : null);
        s.setCurrentAmount(pb.getCurrentAmount() != null ? pb.getCurrentAmount().doubleValue() : 0.0);
        s.setStatus(pb.getStatus() != null ? pb.getStatus().name() : null);
        s.setCreatedAt(pb.getCreatedAt() != null ? pb.getCreatedAt().toString() : null);
        String imageUrl = null;
        if (pb.getName() != null) {
            String nome = pb.getName().trim().toLowerCase();
            if (nome.equals("carro")) imageUrl = "/cofrinhos/Carro.png";
            else if (nome.equals("casa")) imageUrl = "/cofrinhos/Casa.png";
            else if (nome.equals("lazer")) imageUrl = "/cofrinhos/Lazer.png";
            else if (nome.equals("viagem")) imageUrl = "/cofrinhos/Viagem.png";
        }
        s.setImageUrl(imageUrl);
        return s;
    }
}
