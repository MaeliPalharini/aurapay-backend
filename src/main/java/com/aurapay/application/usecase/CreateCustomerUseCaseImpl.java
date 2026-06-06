package com.aurapay.application.usecase;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.common.validation.CpfValidator;
import com.aurapay.domain.model.Customer;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletStatus;
import com.aurapay.domain.port.in.CreateCustomerUseCase;
import com.aurapay.domain.port.out.CustomerRepositoryPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCustomerUseCaseImpl.class);

    private final CustomerRepositoryPort customerRepositoryPort;
    private final WalletRepositoryPort walletRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public CreateCustomerUseCaseImpl(
            CustomerRepositoryPort customerRepositoryPort,
            WalletRepositoryPort walletRepositoryPort,
            PasswordEncoder passwordEncoder
    ) {
        this.customerRepositoryPort = customerRepositoryPort;
        this.walletRepositoryPort = walletRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public CreateCustomerResponse execute(CreateCustomerRequest request) {
        validateRequest(request);
        validateCustomerUniqueness(request);

        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setDocumentNumber(request.getDocumentNumber());
        customer.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        Customer savedCustomer = customerRepositoryPort.save(customer);
        log.info("Customer salvo com ID: {}", savedCustomer.getId());

        Wallet wallet = new Wallet();
        wallet.setCustomerId(savedCustomer.getId());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setStatus(WalletStatus.ACTIVE);

        log.info("Tentando salvar wallet para customerId: {}", savedCustomer.getId());

        Wallet savedWallet = walletRepositoryPort.save(wallet);
        log.info("Wallet salva com ID: {}", savedWallet.getId());

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
        if (request == null) {
            throw new BusinessException("Request body is required");
        }

        if (request.getFullName() == null || request.getFullName().isBlank()) {
            throw new BusinessException("Full name is required");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("Email is required");
        }

        if (request.getDocumentNumber() == null || request.getDocumentNumber().isBlank()) {
            throw new BusinessException("Document number is required");
        }

        if (!request.getDocumentNumber().matches("\\d{11}")) {
            throw new BusinessException("CPF deve conter exatamente 11 dígitos numéricos, sem pontos ou traços");
        }

        if (!CpfValidator.isValid(request.getDocumentNumber())) {
            throw new BusinessException("CPF inválido");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BusinessException("Password is required");
        }

        if (request.getPassword().length() < 6) {
            throw new BusinessException("Password must be at least 6 characters long");
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