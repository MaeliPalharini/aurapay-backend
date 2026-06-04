package com.aurapay.application.usecase;

import com.aurapay.application.dto.StatementEntryResponse;
import com.aurapay.application.dto.StatementResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.port.in.GetStatementUseCase;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.port.out.WalletTransactionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Monta o extrato lendo o ledger (wallet_transactions), que é a fonte única
 * de movimentações da carteira. É somente leitura.
 */
@Service
public class GetStatementUseCaseImpl implements GetStatementUseCase {

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;

    public GetStatementUseCaseImpl(
            WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort walletTransactionRepository
    ) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public StatementResponse execute(Long customerId) {
        if (customerId == null) {
            throw new BusinessException("customerId é obrigatório");
        }

        Wallet wallet = walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o cliente"));

        List<StatementEntryResponse> entries = walletTransactionRepository
                .findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(t -> new StatementEntryResponse(
                        "TX-" + t.getId(),
                        t.getType().name(),
                        t.getDirection().name(),
                        t.getDescription(),
                        t.getAmount(),
                        t.getCreatedAt()
                ))
                .toList();

        return new StatementResponse(wallet.getBalance(), entries);
    }
}
