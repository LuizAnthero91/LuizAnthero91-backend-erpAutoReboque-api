CREATE TABLE permissoes (
                            id BIGSERIAL PRIMARY KEY,
                            codigo VARCHAR(100) NOT NULL UNIQUE,
                            descricao VARCHAR(255)
);

CREATE TABLE perfis (
                        id BIGSERIAL PRIMARY KEY,
                        nome VARCHAR(100) NOT NULL UNIQUE,
                        descricao VARCHAR(255),
                        ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE perfil_permissoes (
                                   perfil_id BIGINT NOT NULL,
                                   permissao_id BIGINT NOT NULL,

                                   PRIMARY KEY (perfil_id, permissao_id),

                                   CONSTRAINT fk_perfil_permissoes_perfil
                                       FOREIGN KEY (perfil_id)
                                           REFERENCES perfis(id),

                                   CONSTRAINT fk_perfil_permissoes_permissao
                                       FOREIGN KEY (permissao_id)
                                           REFERENCES permissoes(id)
);

ALTER TABLE usuarios
    ADD COLUMN perfil_id BIGINT;

ALTER TABLE usuarios
    ADD CONSTRAINT fk_usuario_perfil
        FOREIGN KEY (perfil_id)
            REFERENCES perfis(id);