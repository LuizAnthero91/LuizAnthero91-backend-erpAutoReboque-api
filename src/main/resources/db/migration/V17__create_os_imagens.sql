CREATE TABLE os_imagens (
                            id BIGSERIAL PRIMARY KEY,

                            ordem_servico_id BIGINT NOT NULL,

                            storage_key VARCHAR(500) NOT NULL UNIQUE,

                            nome_original VARCHAR(255),

                            content_type VARCHAR(100),

                            tamanho_bytes BIGINT,

                            criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_os_imagens_ordem_servico
                                FOREIGN KEY (ordem_servico_id)
                                    REFERENCES ordens_servico(id)
                                    ON DELETE CASCADE
);

CREATE INDEX idx_os_imagens_ordem_servico
    ON os_imagens(ordem_servico_id);