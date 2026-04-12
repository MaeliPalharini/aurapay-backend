package com.aurapay.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String documentNumber; // Vamos usar email e documento para simular o login, sem senha por enquanto
}

