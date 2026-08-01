package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manutencoes")
public class Manutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoManutencao tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusManutencao status;

    @Column(name = "data_manutencao", nullable = false)
    private LocalDate dataManutencao;

    @Column(name = "km_atual")
    private BigDecimal kmAtual;

    @Column(nullable = false)
    private String descricao;

    private String oficina;

    @Column(name = "custo_pecas", nullable = false)
    private BigDecimal custoPecas = BigDecimal.ZERO;

    @Column(name = "custo_mao_obra", nullable = false)
    private BigDecimal custoMaoObra = BigDecimal.ZERO;

    @Column(name = "custo_total", nullable = false)
    private BigDecimal custoTotal = BigDecimal.ZERO;

    @Column(name = "proxima_manutencao_km")
    private BigDecimal proximaManutencaoKm;

    @Column(name = "proxima_manutencao_data")
    private LocalDate proximaManutencaoData;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected Manutencao() {
    }

    public Manutencao(
            Veiculo veiculo,
            TipoManutencao tipo,
            LocalDate dataManutencao,
            BigDecimal kmAtual,
            String descricao,
            String oficina,
            BigDecimal custoPecas,
            BigDecimal custoMaoObra,
            BigDecimal proximaManutencaoKm,
            LocalDate proximaManutencaoData,
            String observacao
    ) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.status = StatusManutencao.ABERTA;
        this.dataManutencao = dataManutencao;
        this.kmAtual = kmAtual;
        this.descricao = descricao;
        this.oficina = oficina;
        this.custoPecas = custoPecas == null ? BigDecimal.ZERO : custoPecas;
        this.custoMaoObra = custoMaoObra == null ? BigDecimal.ZERO : custoMaoObra;
        this.custoTotal = this.custoPecas.add(this.custoMaoObra);
        this.proximaManutencaoKm = proximaManutencaoKm;
        this.proximaManutencaoData = proximaManutencaoData;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public TipoManutencao getTipo() {
        return tipo;
    }

    public StatusManutencao getStatus() {
        return status;
    }

    public LocalDate getDataManutencao() {
        return dataManutencao;
    }

    public BigDecimal getKmAtual() {
        return kmAtual;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getOficina() {
        return oficina;
    }

    public BigDecimal getCustoPecas() {
        return custoPecas;
    }

    public BigDecimal getCustoMaoObra() {
        return custoMaoObra;
    }

    public BigDecimal getCustoTotal() {
        return custoTotal;
    }

    public BigDecimal getProximaManutencaoKm() {
        return proximaManutencaoKm;
    }

    public LocalDate getProximaManutencaoData() {
        return proximaManutencaoData;
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
            TipoManutencao tipo,
            StatusManutencao status,
            LocalDate dataManutencao,
            BigDecimal kmAtual,
            String descricao,
            String oficina,
            BigDecimal custoPecas,
            BigDecimal custoMaoObra,
            BigDecimal proximaManutencaoKm,
            LocalDate proximaManutencaoData,
            String observacao
    ) {
        this.tipo = tipo;
        this.status = status;
        this.dataManutencao = dataManutencao;
        this.kmAtual = kmAtual;
        this.descricao = descricao;
        this.oficina = oficina;
        this.custoPecas = custoPecas == null ? BigDecimal.ZERO : custoPecas;
        this.custoMaoObra = custoMaoObra == null ? BigDecimal.ZERO : custoMaoObra;
        this.custoTotal = this.custoPecas.add(this.custoMaoObra);
        this.proximaManutencaoKm = proximaManutencaoKm;
        this.proximaManutencaoData = proximaManutencaoData;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void iniciar() {
        this.status = StatusManutencao.EM_ANDAMENTO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void concluir() {
        this.status = StatusManutencao.CONCLUIDA;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void cancelar() {
        this.status = StatusManutencao.CANCELADA;
        this.atualizadoEm = LocalDateTime.now();
    }
}