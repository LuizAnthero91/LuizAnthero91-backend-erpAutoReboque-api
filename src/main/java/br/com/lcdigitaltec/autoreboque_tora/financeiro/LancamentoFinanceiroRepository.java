package br.com.lcdigitaltec.autoreboque_tora.financeiro;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LancamentoFinanceiroRepository extends JpaRepository<LancamentoFinanceiro, Long> {

    List<LancamentoFinanceiro> findByTipo(TIpoLancamento tipo);

    List<LancamentoFinanceiro> findByVeiculoId(Long veiculoId);

    List<LancamentoFinanceiro> findByOrdemServicoId(Long ordemServicoId);

    boolean existsByOrdemServicoIdAndTipo(Long ordemServicoId, TIpoLancamento tipo);

    @Query("""
            SELECT COALESCE(SUM(l.valor), 0)
            FROM LancamentoFinanceiro l
            WHERE l.tipo = :tipo
            AND l.status <> br.com.lcdigitaltec.autoreboque_tora.financeiro.StatusPagamento.CANCELADO
            AND l.dataLancamento BETWEEN :inicio AND :fim
            """)
    BigDecimal somarPorTipoEPeriodo(TIpoLancamento tipo, LocalDate inicio, LocalDate fim);

    @Query("""
            SELECT COALESCE(SUM(l.valor), 0)
            FROM LancamentoFinanceiro l
            WHERE l.categoria = :categoria
            AND l.status <> br.com.lcdigitaltec.autoreboque_tora.financeiro.StatusPagamento.CANCELADO
            AND l.dataLancamento BETWEEN :inicio AND :fim
            """)
    BigDecimal somarPorCategoriaEPeriodo(CategoriaFinanceira categoria, LocalDate inicio, LocalDate fim);

    @Query(value = """
        SELECT
            EXTRACT(MONTH FROM data_lancamento)::int AS mes,
            COALESCE(SUM(CASE WHEN tipo = 'RECEITA' AND status <> 'CANCELADO' THEN valor ELSE 0 END), 0) AS receitas,
            COALESCE(SUM(CASE WHEN tipo = 'DESPESA' AND status <> 'CANCELADO' THEN valor ELSE 0 END), 0) AS despesas
        FROM lancamentos_financeiros
        WHERE EXTRACT(YEAR FROM data_lancamento) = :ano
        GROUP BY EXTRACT(MONTH FROM data_lancamento)
        ORDER BY mes
        """, nativeQuery = true)
    List<FinanceiroMensalProjection> buscarFinanceiroMensal(@Param("ano") int ano);
}