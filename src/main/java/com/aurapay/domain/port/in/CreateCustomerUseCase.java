package com.aurapay.domain.port.in;

import com.aurapay.application.dto.CreateCustomerRequest;
import com.aurapay.application.dto.CreateCustomerResponse;

public interface CreateCustomerUseCase {

    CreateCustomerResponse execute(CreateCustomerRequest request);
}
