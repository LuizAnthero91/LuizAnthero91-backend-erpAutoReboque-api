CREATE TABLE manutencoes (
                             id BIGSERIAL PRIMARY KEY,

                             veiculo_id BIGINT NOT NULL REFERENCES veiculos(id),

                             tipo VARCHAR(50) NOT NULL,
                             status VARCHAR(50) NOT NULL,

                             data_manutencao DATE NOT NULL,
                             km_atual NUMERIC(12,2),

                             descricao TEXT NOT NULL,
                             oficina VARCHAR(120),

                             custo_pecas NUMERIC(12,2) NOT NULL DEFAULT 0,
                             custo_mao_obra NUMERIC(12,2) NOT NULL DEFAULT 0,
                             custo_total NUMERIC(12,2) NOT NULL DEFAULT 0,

                             proxima_manutencao_km NUMERIC(12,2),
                             proxima_manutencao_data DATE,

                             observacao TEXT,

                             criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);