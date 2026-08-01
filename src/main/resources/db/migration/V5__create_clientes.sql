CREATE TABLE clientes (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(150) NOT NULL,
                          documento VARCHAR(30) UNIQUE,
                          telefone VARCHAR(30),
                          email VARCHAR(150),
                          tipo VARCHAR(50) NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          endereco TEXT,
                          observacao TEXT,
                          criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);