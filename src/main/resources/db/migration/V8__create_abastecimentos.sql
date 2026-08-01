CREATE TABLE abastecimentos (
                                id BIGSERIAL PRIMARY KEY,

                                veiculo_id BIGINT NOT NULL REFERENCES veiculos(id),
                                motorista_id BIGINT REFERENCES motoristas(id),

                                data_abastecimento DATE NOT NULL,
                                km_atual NUMERIC(12,2) NOT NULL,
                                litros NUMERIC(10,2) NOT NULL,
                                valor_litro NUMERIC(10,2) NOT NULL,
                                valor_total NUMERIC(12,2) NOT NULL,

                                posto VARCHAR(120),
                                observacao TEXT,

                                criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);