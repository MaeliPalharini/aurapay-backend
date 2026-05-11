CREATE TABLE pix_payments (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    wallet_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    mercado_pago_payment_id VARCHAR(100) UNIQUE,
    qr_code TEXT,
    qr_code_base64 TEXT,
    ticket_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_pix_payments_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_pix_payments_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);

CREATE INDEX idx_pix_payments_customer_id ON pix_payments(customer_id);
CREATE INDEX idx_pix_payments_wallet_id ON pix_payments(wallet_id);
CREATE INDEX idx_pix_payments_status ON pix_payments(status);
