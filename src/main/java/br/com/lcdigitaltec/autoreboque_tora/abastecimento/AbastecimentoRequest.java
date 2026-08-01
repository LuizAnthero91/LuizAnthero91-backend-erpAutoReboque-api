package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoRequest(

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

        Long motoristaId,

        @NotNull(message = "Data do abastecimento é obrigatória")
        LocalDate dataAbastecimento,

        @NotNull(message = "Km atual é obrigatório")
        @PositiveOrZero(message = "Km atual não pode ser negativo")
        BigDecimal kmAtual,

        @NotNull(message = "Litros é obrigatório")
        @Positive(message = "Litros precisa ser maior que zero")
        BigDecimal litros,

        @NotNull(message = "Valor por litro é obrigatório")
        @Positive(message = "Valor por litro precisa ser maior que zero")
        BigDecimal valorLitro,

        String posto,

        String observacao
) {
}