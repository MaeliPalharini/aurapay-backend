package com.aurapay.application.usecase;

import com.aurapay.application.dto.LoginRequest;
import com.aurapay.application.dto.LoginResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Customer;
import com.aurapay.domain.port.in.LoginUseCase;
import com.aurapay.domain.port.out.CustomerRepositoryPort;
import com.aurapay.domain.port.out.TokenProviderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl implements LoginUseCase {

    private final CustomerRepositoryPort customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProviderPort tokenProvider;

    public LoginUseCaseImpl(
            CustomerRepositoryPort customerRepository,
            PasswordEncoder passwordEncoder,
            TokenProviderPort tokenProvider
    ) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public LoginResponse execute(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BusinessException("E-mail é obrigatório para login");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BusinessException("Senha é obrigatória para login");
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciais inválidas."));

        if (customer.getPasswordHash() == null
                || !passwordEncoder.matches(request.getPassword(), customer.getPasswordHash())) {
            throw new BusinessException("Credenciais inválidas.");
        }

        LoginResponse response = new LoginResponse();
        response.setCustomerId(customer.getId());
        response.setFullName(customer.getFullName());
        response.setEmail(customer.getEmail());
        response.setToken(tokenProvider.generateToken(customer.getId(), customer.getEmail()));

        return response;
    }
}
