CREATE TABLE veiculos (
                          id BIGSERIAL PRIMARY KEY,
                          placa VARCHAR(20) NOT NULL UNIQUE,
                          marca VARCHAR(80) NOT NULL,
                          modelo VARCHAR(100) NOT NULL,
                          ano INTEGER,
                          tipo VARCHAR(50) NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          km_atual NUMERIC(12,2) NOT NULL DEFAULT 0,
                          observacao TEXT,
                          criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);