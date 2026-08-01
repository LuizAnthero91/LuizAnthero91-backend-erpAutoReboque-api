package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AbastecimentoResponse(
        Long id,

        Long veiculoId,
        String veiculoPlaca,
        String veiculoModelo,

        Long motoristaId,
        String motoristaNome,

        LocalDate dataAbastecimento,
        BigDecimal kmAtual,
        BigDecimal litros,
        BigDecimal valorLitro,
        BigDecimal valorTotal,

        String posto,
        String observacao,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public AbastecimentoResponse(Abastecimento abastecimento) {
        this(
                abastecimento.getId(),

                abastecimento.getVeiculo().getId(),
                abastecimento.getVeiculo().getPlaca(),
                abastecimento.getVeiculo().getModelo(),

                abastecimento.getMotorista() == null ? null : abastecimento.getMotorista().getId(),
                abastecimento.getMotorista() == null ? null : abastecimento.getMotorista().getNome(),

                abastecimento.getDataAbastecimento(),
                abastecimento.getKmAtual(),
                abastecimento.getLitros(),
                abastecimento.getValorLitro(),
                abastecimento.getValorTotal(),

                abastecimento.getPosto(),
                abastecimento.getObservacao(),

                abastecimento.getCriadoEm(),
                abastecimento.getAtualizadoEm()
        );
    }
}
