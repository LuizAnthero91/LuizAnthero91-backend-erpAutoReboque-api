package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import br.com.lcdigitaltec.autoreboque_tora.cliente.Cliente;
import br.com.lcdigitaltec.autoreboque_tora.motorista.Motorista;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ordens_servico",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ordens_servico_numero_os",
                        columnNames = "numero_os"
                )
        }
)
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "numero_os",
            nullable = false,
            unique = true,
            updatable = false
    )
    private Long numeroOs;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servico", nullable = false)
    private TipoServico tipoServico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdemServico status;

    @Column(nullable = false)
    private String origem;

    private String destino;

    @Column(name = "km_estimado")
    private BigDecimal kmEstimado;

    @Column(name = "km_real")
    private BigDecimal kmReal;

    @Column(
            name = "valor_cobrado",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal valorCobrado = BigDecimal.ZERO;

    @Column(
            name = "custo_estimado",
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal custoEstimado = BigDecimal.ZERO;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    protected OrdemServico() {
    }

    public OrdemServico(
            Long numeroOs,
            Cliente cliente,
            Veiculo veiculo,
            Motorista motorista,
            TipoServico tipoServico,
            String origem,
            String destino,
            BigDecimal kmEstimado,
            BigDecimal valorCobrado,
            BigDecimal custoEstimado,
            String observacao
    ) {
        if (numeroOs == null) {
            throw new IllegalArgumentException(
                    "O número da ordem de serviço é obrigatório."
            );
        }

        this.numeroOs = numeroOs;
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.tipoServico = tipoServico;
        this.status = StatusOrdemServico.ABERTA;
        this.origem = origem;
        this.destino = destino;
        this.kmEstimado = kmEstimado;
        this.valorCobrado = valorCobrado == null
                ? BigDecimal.ZERO
                : valorCobrado;
        this.custoEstimado = custoEstimado == null
                ? BigDecimal.ZERO
                : custoEstimado;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Long getNumeroOs() {
        return numeroOs;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public StatusOrdemServico getStatus() {
        return status;
    }

    public String getOrigem() {
        return origem;
    }

    public String getDestino() {
        return destino;
    }

    public BigDecimal getKmEstimado() {
        return kmEstimado;
    }

    public BigDecimal getKmReal() {
        return kmReal;
    }

    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    public BigDecimal getCustoEstimado() {
        return custoEstimado;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public LocalDateTime getDataConclusao() {
        return dataConclusao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void atualizar(
            Cliente cliente,
            Veiculo veiculo,
            Motorista motorista,
            TipoServico tipoServico,
            StatusOrdemServico status,
            String origem,
            String destino,
            BigDecimal kmEstimado,
            BigDecimal kmReal,
            BigDecimal valorCobrado,
            BigDecimal custoEstimado,
            String observacao
    ) {
        this.cliente = cliente;
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.tipoServico = tipoServico;
        this.status = status;
        this.origem = origem;
        this.destino = destino;
        this.kmEstimado = kmEstimado;
        this.kmReal = kmReal;
        this.valorCobrado = valorCobrado == null
                ? BigDecimal.ZERO
                : valorCobrado;
        this.custoEstimado = custoEstimado == null
                ? BigDecimal.ZERO
                : custoEstimado;
        this.observacao = observacao;
    }

    public void iniciarAtendimento() {
        this.status = StatusOrdemServico.EM_ATENDIMENTO;
    }

    public void concluir(BigDecimal kmReal) {
        this.status = StatusOrdemServico.CONCLUIDA;
        this.kmReal = kmReal;
        this.dataConclusao = LocalDateTime.now();
    }

    public void faturar() {
        this.status = StatusOrdemServico.FATURADA;
    }

    public void cancelar() {
        this.status = StatusOrdemServico.CANCELADA;
    }
}