package com.aurapay.domain.port.in;

public interface DeletePixKeyUseCase {
    void execute(Long pixKeyId, Long customerId);
}