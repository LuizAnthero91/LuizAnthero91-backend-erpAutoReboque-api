CREATE TABLE motoristas (
                            id BIGSERIAL PRIMARY KEY,
                            nome VARCHAR(120) NOT NULL,
                            cpf VARCHAR(20) UNIQUE,
                            telefone VARCHAR(30),
                            cnh VARCHAR(40) UNIQUE,
                            categoria_cnh VARCHAR(10),
                            validade_cnh DATE,
                            status VARCHAR(50) NOT NULL,
                            observacao TEXT,
                            criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);