package br.com.lcdigitaltec.autoreboque_tora.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record VeiculoRequest(

        @NotBlank(message = "Placa é obrigatória")
        String placa,

        @NotBlank(message = "Marca é obrigatória")
        String marca,

        @NotBlank(message = "Modelo é obrigatório")
        String modelo,

        Integer ano,

        @NotNull(message = "Tipo do veículo é obrigatório")
        TipoVeiculo tipo,

        StatusVeiculo status,

        @PositiveOrZero(message = "Km atual não pode ser negativo")
        BigDecimal kmAtual,

        String observacao
) {
}
