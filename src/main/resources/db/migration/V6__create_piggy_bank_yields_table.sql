
CREATE TABLE IF NOT EXISTS piggy_bank_yields (
    id BIGSERIAL PRIMARY KEY,
    piggy_bank_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    yield_amount NUMERIC(19, 2) NOT NULL,
    yield_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_piggy_bank_yields_piggy_bank
        FOREIGN KEY (piggy_bank_id)
            REFERENCES piggy_banks(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_piggy_bank_yields_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers(id)
            ON DELETE CASCADE
);

-- Um rendimento por dia por cofrinho (evita duplicidade do job)
CREATE UNIQUE INDEX IF NOT EXISTS ux_piggy_bank_yields_piggy_bank_date
    ON piggy_bank_yields(piggy_bank_id, yield_date);

CREATE INDEX IF NOT EXISTS idx_piggy_bank_yields_customer_id ON piggy_bank_yields(customer_id);
CREATE INDEX IF NOT EXISTS idx_piggy_bank_yields_yield_date ON piggy_bank_yields(yield_date);

