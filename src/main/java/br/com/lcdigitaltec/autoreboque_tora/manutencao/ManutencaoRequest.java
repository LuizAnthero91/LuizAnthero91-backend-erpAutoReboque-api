package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoRequest(

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

        @NotNull(message = "Tipo de manutenção é obrigatório")
        TipoManutencao tipo,

        StatusManutencao status,

        @NotNull(message = "Data da manutenção é obrigatória")
        LocalDate dataManutencao,

        @PositiveOrZero(message = "Km atual não pode ser negativo")
        BigDecimal kmAtual,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        String oficina,

        @PositiveOrZero(message = "Custo de peças não pode ser negativo")
        BigDecimal custoPecas,

        @PositiveOrZero(message = "Custo de mão de obra não pode ser negativo")
        BigDecimal custoMaoObra,

        @PositiveOrZero(message = "Próxima manutenção por km não pode ser negativa")
        BigDecimal proximaManutencaoKm,

        LocalDate proximaManutencaoData,

        String observacao
) {
}
