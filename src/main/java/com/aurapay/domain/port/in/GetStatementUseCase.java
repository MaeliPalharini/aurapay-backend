package com.aurapay.domain.port.in;

import com.aurapay.application.dto.StatementResponse;

public interface GetStatementUseCase {
    StatementResponse execute(Long customerId);
}
