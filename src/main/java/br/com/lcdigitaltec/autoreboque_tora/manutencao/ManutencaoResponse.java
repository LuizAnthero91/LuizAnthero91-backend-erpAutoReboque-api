package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ManutencaoResponse(
        Long id,

        Long veiculoId,
        String veiculoPlaca,
        String veiculoModelo,

        TipoManutencao tipo,
        StatusManutencao status,

        LocalDate dataManutencao,
        BigDecimal kmAtual,

        String descricao,
        String oficina,

        BigDecimal custoPecas,
        BigDecimal custoMaoObra,
        BigDecimal custoTotal,

        BigDecimal proximaManutencaoKm,
        LocalDate proximaManutencaoData,

        String observacao,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public ManutencaoResponse(Manutencao manutencao) {
        this(
                manutencao.getId(),

                manutencao.getVeiculo().getId(),
                manutencao.getVeiculo().getPlaca(),
                manutencao.getVeiculo().getModelo(),

                manutencao.getTipo(),
                manutencao.getStatus(),

                manutencao.getDataManutencao(),
                manutencao.getKmAtual(),

                manutencao.getDescricao(),
                manutencao.getOficina(),

                manutencao.getCustoPecas(),
                manutencao.getCustoMaoObra(),
                manutencao.getCustoTotal(),

                manutencao.getProximaManutencaoKm(),
                manutencao.getProximaManutencaoData(),

                manutencao.getObservacao(),

                manutencao.getCriadoEm(),
                manutencao.getAtualizadoEm()
        );
    }
}