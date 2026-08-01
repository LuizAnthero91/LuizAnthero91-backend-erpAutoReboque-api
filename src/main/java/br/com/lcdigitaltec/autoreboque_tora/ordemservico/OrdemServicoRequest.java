package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record OrdemServicoRequest(

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        Long veiculoId,

        Long motoristaId,

        @NotNull(message = "Tipo de serviço é obrigatório")
        TipoServico tipoServico,

        StatusOrdemServico status,

        @NotBlank(message = "Origem é obrigatória")
        String origem,

        String destino,

        @PositiveOrZero(message = "Km estimado não pode ser negativo")
        BigDecimal kmEstimado,

        @PositiveOrZero(message = "Km real não pode ser negativo")
        BigDecimal kmReal,

        @PositiveOrZero(message = "Valor cobrado não pode ser negativo")
        BigDecimal valorCobrado,

        @PositiveOrZero(message = "Custo estimado não pode ser negativo")
        BigDecimal custoEstimado,

        String observacao
) {
}
