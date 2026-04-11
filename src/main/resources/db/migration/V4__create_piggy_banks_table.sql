
CREATE TABLE IF NOT EXISTS piggy_banks (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    target_amount NUMERIC(19, 2),
    current_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    transaction_count BIGINT NOT NULL DEFAULT 0,
    last_transaction_at TIMESTAMP,
    total_yield_amount NUMERIC(19, 2) NOT NULL DEFAULT 0,
    last_yield_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_piggy_banks_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers(id)
            ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_piggy_banks_customer_id ON piggy_banks(customer_id);
CREATE INDEX IF NOT EXISTS idx_piggy_banks_status ON piggy_banks(status);

