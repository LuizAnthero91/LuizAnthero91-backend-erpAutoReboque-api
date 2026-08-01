package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LancamentoFinanceiroRequest(

        Long veiculoId,

        Long ordemServicoId,

        @NotNull(message = "Tipo do lançamento é obrigatório")
        TIpoLancamento tipo,

        @NotNull(message = "Categoria é obrigatória")
        CategoriaFinanceira categoria,

        StatusPagamento status,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor precisa ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Data do lançamento é obrigatória")
        LocalDate dataLancamento,

        String observacao
) {
}