CREATE TABLE lancamentos_financeiros (
                                         id BIGSERIAL PRIMARY KEY,

                                         veiculo_id BIGINT REFERENCES veiculos(id),
                                         ordem_servico_id BIGINT REFERENCES ordens_servico(id),

                                         tipo VARCHAR(50) NOT NULL,
                                         categoria VARCHAR(80) NOT NULL,
                                         status VARCHAR(50) NOT NULL,

                                         descricao TEXT NOT NULL,
                                         valor NUMERIC(12,2) NOT NULL,
                                         data_lancamento DATE NOT NULL,

                                         observacao TEXT,

                                         criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);