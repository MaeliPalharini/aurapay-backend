package com.aurapay.application.usecase;

import com.aurapay.application.dto.LoginRequest;
import com.aurapay.application.dto.LoginResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Customer;
import com.aurapay.domain.port.in.LoginUseCase;
import com.aurapay.domain.port.out.CustomerRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final CustomerRepositoryPort customerRepository;

    public LoginUseCaseImpl(CustomerRepositoryPort customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public LoginResponse execute(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("E-mail é obrigatório para login");
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas."));

        if (request.getDocumentNumber() != null && !request.getDocumentNumber().isBlank()) {
            if (!customer.getDocumentNumber().equals(request.getDocumentNumber())) {
                throw new BusinessException("Credenciais inválidas.");
            }
        }

        LoginResponse response = new LoginResponse();
        response.setCustomerId(customer.getId());
        response.setFullName(customer.getFullName());
        response.setEmail(customer.getEmail());

        return response;
    }
}

