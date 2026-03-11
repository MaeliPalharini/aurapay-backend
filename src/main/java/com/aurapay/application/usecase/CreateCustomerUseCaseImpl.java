package com.aurapay.application.usecase;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Customer;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletStatus;
import com.aurapay.domain.port.in.CreateCustomerUseCase;
import com.aurapay.domain.port.out.CustomerRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private final CustomerRepositoryPort customerRepositoryPort;
    private final WalletRepositoryPort walletRepositoryPort;

    public CreateCustomerUseCaseImpl(
            CustomerRepositoryPort customerRepositoryPort,
            WalletRepositoryPort walletRepositoryPort
    ) {
        this.customerRepositoryPort = customerRepositoryPort;
        this.walletRepositoryPort = walletRepositoryPort;
    }

    @Override
    public CreateCustomerResponse execute(CreateCustomerRequest request) {
        validateRequest(request);
        validateCustomerUniqueness(request);

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setDocumentNumber(request.getDocumentNumber());

        Customer savedCustomer = customerRepositoryPort.save(customer);

        Wallet wallet = new Wallet();
        wallet.setCustomerId(savedCustomer.getId());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setStatus(WalletStatus.ACTIVE);

        Wallet savedWallet = walletRepositoryPort.save(wallet);

        CreateCustomerResponse response = new CreateCustomerResponse();
        response.setCustomerId(savedCustomer.getId());
        response.setFullName(savedCustomer.getFullName());
        response.setEmail(savedCustomer.getEmail());
        response.setDocumentNumber(savedCustomer.getDocumentNumber());
        response.setWalletId(savedWallet.getId());
        response.setBalance(savedWallet.getBalance());
        response.setWalletStatus(savedWallet.getStatus().name());

        return response;
    }

    private void validateRequest(CreateCustomerRequest request) {
        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new BusinessException("Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }

        if (request.getDocumentNumber() == null || request.getDocumentNumber().isBlank()) {
            throw new BusinessException("Document number is required");
        }
    }

    private void validateCustomerUniqueness(CreateCustomerRequest request) {
        if (customerRepositoryPort.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessException("Email already registered");
        }

        if (customerRepositoryPort.findByDocumentNumber(request.getDocumentNumber()).isPresent()) {
            throw new BusinessException("Document number already registered");
        }
    }
}
