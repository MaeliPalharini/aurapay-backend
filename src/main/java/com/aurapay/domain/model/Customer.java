package com.aurapay.domain.model;
// Ele representa o cliente no domínio.
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Customer {

    private Long id;
    private String fullName;
    private String email;
    private String documentNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}