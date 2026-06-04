package com.aurapay.adapter.in.web.controller;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;
import com.aurapay.application.dto.GetWalletResponse;
import com.aurapay.application.dto.DepositToWalletRequest;
import com.aurapay.application.dto.DepositToWalletResponse;
import com.aurapay.domain.port.in.CreateCustomerUseCase;
import com.aurapay.domain.port.in.GetWalletByCustomerIdUseCase;
import com.aurapay.domain.port.in.DepositToWalletUseCase;
import com.aurapay.domain.port.in.GetStatementUseCase;
import com.aurapay.domain.port.in.LoginUseCase;
import com.aurapay.application.dto.LoginRequest;
import com.aurapay.application.dto.LoginResponse;
import com.aurapay.application.dto.StatementResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase;
    private final DepositToWalletUseCase depositToWalletUseCase;
    private final LoginUseCase loginUseCase;
    private final GetStatementUseCase getStatementUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase,
            DepositToWalletUseCase depositToWalletUseCase,
            LoginUseCase loginUseCase,
            GetStatementUseCase getStatementUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getWalletByCustomerIdUseCase = getWalletByCustomerIdUseCase;
        this.depositToWalletUseCase = depositToWalletUseCase;
        this.loginUseCase = loginUseCase;
        this.getStatementUseCase = getStatementUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
        return createCustomerUseCase.execute(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest request) {
        return loginUseCase.execute(request);
    }

    @GetMapping("/{customerId}/wallet")
    @ResponseStatus(HttpStatus.OK)
    public GetWalletResponse getWalletByCustomerId(@PathVariable Long customerId) {
        return getWalletByCustomerIdUseCase.execute(customerId);
    }

    @GetMapping("/{customerId}/extrato")
    @ResponseStatus(HttpStatus.OK)
    public StatementResponse getStatement(@PathVariable Long customerId) {
        return getStatementUseCase.execute(customerId);
    }

    @PostMapping("/{customerId}/wallet/deposit")
    @ResponseStatus(HttpStatus.OK)
    public DepositToWalletResponse depositToWallet(
            @PathVariable Long customerId,
            @RequestBody DepositToWalletRequest request
    ) {
        request.setCustomerId(customerId);
        return depositToWalletUseCase.execute(request);
    }

}