CREATE TABLE documentos_veiculos (
                                     id BIGSERIAL PRIMARY KEY,

                                     veiculo_id BIGINT NOT NULL REFERENCES veiculos(id),

                                     tipo VARCHAR(60) NOT NULL,
                                     status VARCHAR(50) NOT NULL,

                                     numero_documento VARCHAR(100),
                                     data_emissao DATE,
                                     data_vencimento DATE NOT NULL,

                                     valor NUMERIC(12,2) NOT NULL DEFAULT 0,

                                     orgao_emissor VARCHAR(120),
                                     arquivo_url TEXT,

                                     despesa_gerada BOOLEAN NOT NULL DEFAULT FALSE,

                                     observacao TEXT,

                                     criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     atualizado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_documentos_veiculos_veiculo_id ON documentos_veiculos(veiculo_id);
CREATE INDEX idx_documentos_veiculos_data_vencimento ON documentos_veiculos(data_vencimento);
CREATE INDEX idx_documentos_veiculos_status ON documentos_veiculos(status);