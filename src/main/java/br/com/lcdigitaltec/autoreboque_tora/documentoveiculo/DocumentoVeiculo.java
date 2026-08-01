package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_veiculos")
public class DocumentoVeiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumentoVeiculo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDocumentoVeiculo status;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "data_emissao")
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(name = "orgao_emissor")
    private String orgaoEmissor;

    @Column(name = "arquivo_url")
    private String arquivoUrl;

    @Column(name = "despesa_gerada", nullable = false)
    private boolean despesaGerada = false;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected DocumentoVeiculo() {
    }

    public DocumentoVeiculo(
            Veiculo veiculo,
            TipoDocumentoVeiculo tipo,
            StatusDocumentoVeiculo status,
            String numeroDocumento,
            LocalDate dataEmissao,
            LocalDate dataVencimento,
            BigDecimal valor,
            String orgaoEmissor,
            String arquivoUrl,
            String observacao
    ) {
        this.veiculo = veiculo;
        this.tipo = tipo;
        this.status = status == null ? calcularStatus(dataVencimento) : status;
        this.numeroDocumento = numeroDocumento;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.valor = valor == null ? BigDecimal.ZERO : valor;
        this.orgaoEmissor = orgaoEmissor;
        this.arquivoUrl = arquivoUrl;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public TipoDocumentoVeiculo getTipo() {
        return tipo;
    }

    public StatusDocumentoVeiculo getStatus() {
        return status;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public String getArquivoUrl() {
        return arquivoUrl;
    }

    public boolean isDespesaGerada() {
        return despesaGerada;
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
            TipoDocumentoVeiculo tipo,
            StatusDocumentoVeiculo status,
            String numeroDocumento,
            LocalDate dataEmissao,
            LocalDate dataVencimento,
            BigDecimal valor,
            String orgaoEmissor,
            String arquivoUrl,
            String observacao
    ) {
        this.tipo = tipo;
        this.status = status == null ? calcularStatus(dataVencimento) : status;
        this.numeroDocumento = numeroDocumento;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.valor = valor == null ? BigDecimal.ZERO : valor;
        this.orgaoEmissor = orgaoEmissor;
        this.arquivoUrl = arquivoUrl;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void cancelar() {
        this.status = StatusDocumentoVeiculo.CANCELADO;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void marcarDespesaGerada() {
        this.despesaGerada = true;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void atualizarStatusPorVencimento() {
        if (this.status == StatusDocumentoVeiculo.CANCELADO) {
            return;
        }

        this.status = calcularStatus(this.dataVencimento);
        this.atualizadoEm = LocalDateTime.now();
    }

    private StatusDocumentoVeiculo calcularStatus(LocalDate dataVencimento) {
        LocalDate hoje = LocalDate.now();

        if (dataVencimento.isBefore(hoje)) {
            return StatusDocumentoVeiculo.VENCIDO;
        }

        if (!dataVencimento.isAfter(hoje.plusDays(30))) {
            return StatusDocumentoVeiculo.A_VENCER;
        }

        return StatusDocumentoVeiculo.VALIDO;
    }
}