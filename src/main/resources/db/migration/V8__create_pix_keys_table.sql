CREATE TABLE pix_keys (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    key_value VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_pix_keys_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE INDEX idx_pix_keys_customer_id ON pix_keys(customer_id);