package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ConcluirOrdemServicoRequest(

        @PositiveOrZero(message = "Km real não pode ser negativo")
        BigDecimal kmReal
) {
}