ALTER TABLE ordens_servico
    ADD COLUMN veiculo_cliente_placa VARCHAR(10),
ADD COLUMN veiculo_cliente_marca VARCHAR(80),
ADD COLUMN veiculo_cliente_modelo VARCHAR(100),
ADD COLUMN veiculo_cliente_cor VARCHAR(50),
ADD COLUMN veiculo_cliente_ano INTEGER,
ADD COLUMN veiculo_cliente_km NUMERIC(12,2),
ADD COLUMN veiculo_cliente_observacao TEXT;

CREATE INDEX idx_ordens_servico_veiculo_cliente_placa
    ON ordens_servico (veiculo_cliente_placa);