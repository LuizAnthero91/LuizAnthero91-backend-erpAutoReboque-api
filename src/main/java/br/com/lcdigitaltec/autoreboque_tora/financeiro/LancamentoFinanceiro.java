package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServico;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamentos_financeiros")
public class LancamentoFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TIpoLancamento tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaFinanceira categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamento status;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_lancamento", nullable = false)
    private LocalDate dataLancamento;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected LancamentoFinanceiro() {
    }

    public LancamentoFinanceiro(
            Veiculo veiculo,
            OrdemServico ordemServico,
            TIpoLancamento tipo,
            CategoriaFinanceira categoria,
            StatusPagamento status,
            String descricao,
            BigDecimal valor,
            LocalDate dataLancamento,
            String observacao
    ) {
        this.veiculo = veiculo;
        this.ordemServico = ordemServico;
        this.tipo = tipo;
        this.categoria = categoria;
        this.status = status == null ? StatusPagamento.PENDENTE : status;
        this.descricao = descricao;
        this.valor = valor;
        this.dataLancamento = dataLancamento;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public TIpoLancamento getTipo() {
        return tipo;
    }

    public CategoriaFinanceira getCategoria() {
        return categoria;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void atualizar(
            Veiculo veiculo,
            OrdemServico ordemServico,
            TIpoLancamento tipo,
            CategoriaFinanceira categoria,
            StatusPagamento status,
            String descricao,
            BigDecimal valor,
            LocalDate dataLancamento,
            String observacao
    ) {
        this.veiculo = veiculo;
        this.ordemServico = ordemServico;
        this.tipo = tipo;
        this.categoria = categoria;
        this.status = status;
        this.descricao = descricao;
        this.valor = valor;
        this.dataLancamento = dataLancamento;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void marcarComoPago() {
        this.status = StatusPagamento.PAGO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void cancelar() {
        this.status = StatusPagamento.CANCELADO;
        this.atualizadoEm = LocalDateTime.now();
    }
}
