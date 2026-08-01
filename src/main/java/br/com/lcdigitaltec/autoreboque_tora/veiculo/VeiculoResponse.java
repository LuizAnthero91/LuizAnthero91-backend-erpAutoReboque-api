package br.com.lcdigitaltec.autoreboque_tora.veiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record VeiculoResponse(
        Long id,
        String placa,
        String marca,
        String modelo,
        Integer ano,
        TipoVeiculo tipo,
        StatusVeiculo status,
        BigDecimal kmAtual,
        String observacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public VeiculoResponse(Veiculo veiculo) {
        this(
                veiculo.getId(),
                veiculo.getPlaca(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getAno(),
                veiculo.getTipo(),
                veiculo.getStatus(),
                veiculo.getKmAtual(),
                veiculo.getObservacao(),
                veiculo.getCriadoEm(),
                veiculo.getAtualizadoEm()
        );
    }
}