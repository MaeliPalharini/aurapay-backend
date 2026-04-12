package com.aurapay.domain.port.in;

import com.aurapay.application.dto.LoginRequest;
import com.aurapay.application.dto.LoginResponse;

public interface LoginUseCase {
    LoginResponse execute(LoginRequest request);
}

