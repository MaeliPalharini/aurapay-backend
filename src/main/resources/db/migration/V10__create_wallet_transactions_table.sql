-- Ledger da carteira: fonte única do extrato. Cada movimentação de dinheiro
-- (depósito, Pix recebido/enviado, envio/resgate de cofrinho) gera um registro.
CREATE TABLE wallet_transactions (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    wallet_id BIGINT,
    type VARCHAR(40) NOT NULL,
    direction VARCHAR(10) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    counterpart_name VARCHAR(150),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_transactions_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE INDEX idx_wallet_transactions_customer_id ON wallet_transactions(customer_id);
CREATE INDEX idx_wallet_transactions_created_at ON wallet_transactions(created_at);

-- Backfill: histórico de Pix aprovados vira "Pix recebido"
INSERT INTO wallet_transactions (customer_id, wallet_id, type, direction, amount, description, created_at)
SELECT customer_id, wallet_id, 'PIX_RECEIVED', 'CREDIT', amount, 'Pix recebido', COALESCE(updated_at, created_at)
FROM pix_payments
WHERE status = 'APPROVED'
  AND customer_id IN (SELECT id FROM customers);

-- Backfill: histórico de transações de cofrinho (depósito = saída, resgate = entrada)
INSERT INTO wallet_transactions (customer_id, type, direction, amount, description, created_at)
SELECT customer_id,
       CASE WHEN type = 'DEPOSIT_COFRINHO' THEN 'PIGGY_BANK_DEPOSIT' ELSE 'PIGGY_BANK_WITHDRAW' END,
       CASE WHEN type = 'DEPOSIT_COFRINHO' THEN 'DEBIT' ELSE 'CREDIT' END,
       amount,
       CASE WHEN type = 'DEPOSIT_COFRINHO' THEN 'Valor enviado para o cofrinho' ELSE 'Valor resgatado do cofrinho' END,
       created_at
FROM piggy_bank_transactions
WHERE customer_id IN (SELECT id FROM customers);
