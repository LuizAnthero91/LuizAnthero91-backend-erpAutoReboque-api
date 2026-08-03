CREATE TABLE sequencias_ordem_servico (
                                          ano INTEGER PRIMARY KEY,
                                          ultimo_sequencial INTEGER NOT NULL,

                                          CONSTRAINT ck_sequencia_os_positiva
                                              CHECK (ultimo_sequencial >= 0),

                                          CONSTRAINT ck_sequencia_os_limite
                                              CHECK (ultimo_sequencial <= 9999)
);

ALTER TABLE ordens_servico
    ADD COLUMN numero_os BIGINT;

-- Numera as OS que já existem no banco, seguindo data de abertura e ID.
WITH ordens_numeradas AS (
    SELECT
        id,
        ROW_NUMBER() OVER (
            ORDER BY data_abertura, id
        ) AS sequencial
    FROM ordens_servico
)
UPDATE ordens_servico os
SET numero_os = 20260000 + ordens.sequencial
    FROM ordens_numeradas ordens
WHERE os.id = ordens.id;

-- Inicializa a sequência de 2026 com a quantidade de OS já numeradas.
INSERT INTO sequencias_ordem_servico (
    ano,
    ultimo_sequencial
)
VALUES (
           2026,
           (
               SELECT COUNT(*)
               FROM ordens_servico
           )
       );

ALTER TABLE ordens_servico
    ALTER COLUMN numero_os SET NOT NULL;

ALTER TABLE ordens_servico
    ADD CONSTRAINT uk_ordens_servico_numero_os
        UNIQUE (numero_os);

CREATE INDEX idx_ordens_servico_numero_os
    ON ordens_servico (numero_os);