
CREATE TABLE IF NOT EXISTS piggy_bank_transactions (
    id BIGSERIAL PRIMARY KEY,
    piggy_bank_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    type VARCHAR(40) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_piggy_bank_transactions_piggy_bank
        FOREIGN KEY (piggy_bank_id)
            REFERENCES piggy_banks(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_piggy_bank_transactions_customer
        FOREIGN KEY (customer_id)
            REFERENCES customers(id)
            ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_piggy_bank_transactions_piggy_bank_id ON piggy_bank_transactions(piggy_bank_id);
CREATE INDEX IF NOT EXISTS idx_piggy_bank_transactions_customer_id ON piggy_bank_transactions(customer_id);
CREATE INDEX IF NOT EXISTS idx_piggy_bank_transactions_type ON piggy_bank_transactions(type);
CREATE INDEX IF NOT EXISTS idx_piggy_bank_transactions_created_at ON piggy_bank_transactions(created_at);

