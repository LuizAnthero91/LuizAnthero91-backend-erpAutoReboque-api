package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OrdemServicoRequest(

        @NotNull(message = "Cliente é obrigatório")
        Long clienteId,

        /*
         * Veículo da frota da empresa utilizado no atendimento.
         * Exemplo: o guincho.
         */
        Long veiculoId,

        Long motoristaId,

        @NotNull(message = "Tipo de serviço é obrigatório")
        TipoServico tipoServico,

        StatusOrdemServico status,

        @NotBlank(message = "Origem é obrigatória")
        String origem,

        String destino,

        /*
         * Dados internos da operação.
         * Não precisam aparecer no PDF entregue ao cliente.
         */
        @PositiveOrZero(message = "Km estimado não pode ser negativo")
        BigDecimal kmEstimado,

        @PositiveOrZero(message = "Km real não pode ser negativo")
        BigDecimal kmReal,

        @PositiveOrZero(message = "Valor cobrado não pode ser negativo")
        BigDecimal valorCobrado,

        @PositiveOrZero(message = "Custo estimado não pode ser negativo")
        BigDecimal custoEstimado,

        String observacao,

        /*
         * Dados do veículo pertencente ao cliente.
         */
        @Size(
                max = 10,
                message = "A placa do veículo do cliente deve ter no máximo 10 caracteres"
        )
        @Pattern(
                regexp = "^$|^[A-Za-z0-9-]{7,10}$",
                message = "Informe uma placa válida"
        )
        String veiculoClientePlaca,

        @Size(
                max = 80,
                message = "A marca do veículo deve ter no máximo 80 caracteres"
        )
        String veiculoClienteMarca,

        @Size(
                max = 100,
                message = "O modelo do veículo deve ter no máximo 100 caracteres"
        )
        String veiculoClienteModelo,

        @Size(
                max = 50,
                message = "A cor do veículo deve ter no máximo 50 caracteres"
        )
        String veiculoClienteCor,

        @Min(
                value = 1900,
                message = "O ano do veículo deve ser igual ou posterior a 1900"
        )
        @Max(
                value = 2100,
                message = "O ano do veículo deve ser igual ou anterior a 2100"
        )
        Integer veiculoClienteAno,

        @PositiveOrZero(
                message = "A quilometragem do veículo do cliente não pode ser negativa"
        )
        BigDecimal veiculoClienteKm,

        @Size(
                max = 1000,
                message = "A observação do veículo deve ter no máximo 1000 caracteres"
        )
        String veiculoClienteObservacao
) {
}