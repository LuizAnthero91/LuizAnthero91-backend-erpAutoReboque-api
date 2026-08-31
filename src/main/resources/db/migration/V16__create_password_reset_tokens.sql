CREATE TABLE password_reset_tokens (
                                       id BIGSERIAL PRIMARY KEY,

                                       usuario_id BIGINT NOT NULL,

                                       token_hash VARCHAR(64) NOT NULL UNIQUE,

                                       criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       expira_em TIMESTAMPTZ NOT NULL,

                                       utilizado_em TIMESTAMPTZ,

                                       CONSTRAINT fk_password_reset_usuario
                                           FOREIGN KEY (usuario_id)
                                               REFERENCES usuarios(id)
                                               ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_usuario
    ON password_reset_tokens(usuario_id);

CREATE INDEX idx_password_reset_expira_em
    ON password_reset_tokens(expira_em);