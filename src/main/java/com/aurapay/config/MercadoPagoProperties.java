package com.aurapay.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mercado-pago")
public class MercadoPagoProperties {

    private String baseUrl = "https://api.mercadopago.com";
    private String accessToken;
    private String defaultPayerEmail;
    private boolean mock = false;
}