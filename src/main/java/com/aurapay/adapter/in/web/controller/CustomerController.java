package com.aurapay.adapter.in.web.controller;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;
import com.aurapay.application.dto.GetWalletResponse;
import com.aurapay.domain.port.in.CreateCustomerUseCase;
import com.aurapay.domain.port.in.GetWalletByCustomerIdUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase;

    public CustomerController(
            CreateCustomerUseCase createCustomerUseCase,
            GetWalletByCustomerIdUseCase getWalletByCustomerIdUseCase
    ) {
        this.createCustomerUseCase = createCustomerUseCase;
        this.getWalletByCustomerIdUseCase = getWalletByCustomerIdUseCase;
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
}