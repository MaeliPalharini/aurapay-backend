-- Adiciona a coluna de senha (hash BCrypt) aos clientes.
-- Nullable porque clientes criados antes desta migração não possuem senha;
-- eles não conseguem logar até definir uma. Novos cadastros sempre preenchem.
ALTER TABLE customers ADD COLUMN password_hash VARCHAR(255);
