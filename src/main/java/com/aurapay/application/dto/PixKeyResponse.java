package com.aurapay.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PixKeyResponse {

    private Long id;
    private Long customerId;
    private String keyValue;
    private LocalDateTime createdAt;
}