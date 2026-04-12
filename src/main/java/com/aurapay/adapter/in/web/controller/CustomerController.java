package com.aurapay.adapter.in.web.controller;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;
import com.aurapay.application.dto.GetWalletResponse;
import com.aurapay.application.dto.DepositToWalletRequest;
import com.aurapay.application.dto.DepositToWalletResponse;
import com.aurapay.domain.port.in.CreateCustomerUseCase;
import com.aurapay.domain.port.in.GetWalletByCustomerIdUseCase;
import com.aurapay.domain.port.in.DepositToWalletUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase;
    private final DepositToWalletUseCase depositToWalletUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase,
            DepositToWalletUseCase depositToWalletUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getWalletByCustomerIdUseCase = getWalletByCustomerIdUseCase;
        this.depositToWalletUseCase = depositToWalletUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
        return createCustomerUseCase.execute(request);
    }

    @GetMapping("/{customerId}/wallet")
    @ResponseStatus(HttpStatus.OK)
    public GetWalletResponse getWalletByCustomerId(@PathVariable Long customerId) {
        return getWalletByCustomerIdUseCase.execute(customerId);
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