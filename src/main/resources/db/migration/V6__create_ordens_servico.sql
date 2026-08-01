CREATE TABLE ordens_servico (
                                id BIGSERIAL PRIMARY KEY,

                                cliente_id BIGINT NOT NULL REFERENCES clientes(id),
                                veiculo_id BIGINT REFERENCES veiculos(id),
                                motorista_id BIGINT REFERENCES motoristas(id),

                                tipo_servico VARCHAR(80) NOT NULL,
                                status VARCHAR(50) NOT NULL,

                                origem TEXT NOT NULL,
                                destino TEXT,

                                km_estimado NUMERIC(12,2),
                                km_real NUMERIC(12,2),

                                valor_cobrado NUMERIC(12,2) NOT NULL DEFAULT 0,
                                custo_estimado NUMERIC(12,2) NOT NULL DEFAULT 0,

                                data_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                data_conclusao TIMESTAMP,

                                observacao TEXT
);