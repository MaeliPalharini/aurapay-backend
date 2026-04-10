package com.aurapay.application.usecase;

import com.aurapay.application.dto.GetWalletResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.port.in.GetWalletByCustomerIdUseCase;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetWalletByCustomerIdUseCaseImpl implements GetWalletByCustomerIdUseCase {

    private final WalletRepositoryPort walletRepositoryPort;

    public GetWalletByCustomerIdUseCaseImpl(WalletRepositoryPort walletRepositoryPort) {
        this.walletRepositoryPort = walletRepositoryPort;
    }

    @Override
    public GetWalletResponse execute(Long customerId) {
        Wallet wallet = walletRepositoryPort.findByCustomerId(customerId)
                .orElseThrow(() -> new BusinessException("Wallet not found for customer id: " + customerId));

        return new GetWalletResponse(
                wallet.getId(),
                wallet.getCustomerId(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getUpdatedAt()
        );
    }
}
