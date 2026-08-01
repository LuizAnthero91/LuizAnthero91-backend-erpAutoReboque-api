package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record LancamentoFinanceiroResponse(
        Long id,

        Long veiculoId,
        String veiculoPlaca,

        Long ordemServicoId,

        TIpoLancamento tipo,
        CategoriaFinanceira categoria,
        StatusPagamento status,

        String descricao,
        BigDecimal valor,
        LocalDate dataLancamento,

        String observacao,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public LancamentoFinanceiroResponse(LancamentoFinanceiro lancamento) {
        this(
                lancamento.getId(),

                lancamento.getVeiculo() == null ? null : lancamento.getVeiculo().getId(),
                lancamento.getVeiculo() == null ? null : lancamento.getVeiculo().getPlaca(),

                lancamento.getOrdemServico() == null ? null : lancamento.getOrdemServico().getId(),

                lancamento.getTipo(),
                lancamento.getCategoria(),
                lancamento.getStatus(),

                lancamento.getDescricao(),
                lancamento.getValor(),
                lancamento.getDataLancamento(),

                lancamento.getObservacao(),

                lancamento.getCriadoEm(),
                lancamento.getAtualizadoEm()
        );
    }
}