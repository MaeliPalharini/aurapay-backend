package com.aurapay.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PixKey {

    private Long id;
    private Long customerId;
    private String keyValue;
    private LocalDateTime createdAt;
}
