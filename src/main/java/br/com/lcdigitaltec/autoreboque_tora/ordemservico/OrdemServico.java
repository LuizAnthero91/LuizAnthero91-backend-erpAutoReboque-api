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

    /*
     * Cliente contratante do serviço.
     */
    @ManyToOne(
            optional = false,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "cliente_id",
            nullable = false
    )
    private Cliente cliente;

    /*
     * Veículo da frota da empresa utilizado no atendimento.
     * Exemplo: o guincho responsável pela remoção.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo_servico",
            nullable = false,
            length = 80
    )
    private TipoServico tipoServico;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private StatusOrdemServico status;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String origem;

    @Column(columnDefinition = "TEXT")
    private String destino;

    /*
     * Dados internos da operação.
     * Esses campos não precisam aparecer no PDF entregue ao cliente.
     */
    @Column(
            name = "km_estimado",
            precision = 12,
            scale = 2
    )
    private BigDecimal kmEstimado;

    @Column(
            name = "km_real",
            precision = 12,
            scale = 2
    )
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

    @Column(
            name = "data_abertura",
            nullable = false
    )
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(name = "data_conclusao")
    private LocalDateTime dataConclusao;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    /*
     * Veículo pertencente ao cliente e atendido pela empresa.
     * Não deve ser confundido com o campo "veiculo", que representa
     * o guincho ou outro veículo da frota da empresa.
     */
    @Column(
            name = "veiculo_cliente_placa",
            length = 10
    )
    private String veiculoClientePlaca;

    @Column(
            name = "veiculo_cliente_marca",
            length = 80
    )
    private String veiculoClienteMarca;

    @Column(
            name = "veiculo_cliente_modelo",
            length = 100
    )
    private String veiculoClienteModelo;

    @Column(
            name = "veiculo_cliente_cor",
            length = 50
    )
    private String veiculoClienteCor;

    @Column(name = "veiculo_cliente_ano")
    private Integer veiculoClienteAno;

    @Column(
            name = "veiculo_cliente_km",
            precision = 12,
            scale = 2
    )
    private BigDecimal veiculoClienteKm;

    @Column(
            name = "veiculo_cliente_observacao",
            columnDefinition = "TEXT"
    )
    private String veiculoClienteObservacao;

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
            String observacao,
            String veiculoClientePlaca,
            String veiculoClienteMarca,
            String veiculoClienteModelo,
            String veiculoClienteCor,
            Integer veiculoClienteAno,
            BigDecimal veiculoClienteKm,
            String veiculoClienteObservacao
    ) {
        validarNumeroOs(numeroOs);
        validarCliente(cliente);
        validarTipoServico(tipoServico);
        validarOrigem(origem);

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

        atualizarDadosVeiculoCliente(
                veiculoClientePlaca,
                veiculoClienteMarca,
                veiculoClienteModelo,
                veiculoClienteCor,
                veiculoClienteAno,
                veiculoClienteKm,
                veiculoClienteObservacao
        );
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

    public String getVeiculoClientePlaca() {
        return veiculoClientePlaca;
    }

    public String getVeiculoClienteMarca() {
        return veiculoClienteMarca;
    }

    public String getVeiculoClienteModelo() {
        return veiculoClienteModelo;
    }

    public String getVeiculoClienteCor() {
        return veiculoClienteCor;
    }

    public Integer getVeiculoClienteAno() {
        return veiculoClienteAno;
    }

    public BigDecimal getVeiculoClienteKm() {
        return veiculoClienteKm;
    }

    public String getVeiculoClienteObservacao() {
        return veiculoClienteObservacao;
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
            String observacao,
            String veiculoClientePlaca,
            String veiculoClienteMarca,
            String veiculoClienteModelo,
            String veiculoClienteCor,
            Integer veiculoClienteAno,
            BigDecimal veiculoClienteKm,
            String veiculoClienteObservacao
    ) {
        validarCliente(cliente);
        validarTipoServico(tipoServico);
        validarOrigem(origem);

        this.cliente = cliente;
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.tipoServico = tipoServico;

        if (status != null) {
            this.status = status;
        }

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

        atualizarDadosVeiculoCliente(
                veiculoClientePlaca,
                veiculoClienteMarca,
                veiculoClienteModelo,
                veiculoClienteCor,
                veiculoClienteAno,
                veiculoClienteKm,
                veiculoClienteObservacao
        );
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

    private void atualizarDadosVeiculoCliente(
            String placa,
            String marca,
            String modelo,
            String cor,
            Integer ano,
            BigDecimal km,
            String observacao
    ) {
        this.veiculoClientePlaca = normalizarTextoMaiusculo(placa);
        this.veiculoClienteMarca = normalizarTexto(marca);
        this.veiculoClienteModelo = normalizarTexto(modelo);
        this.veiculoClienteCor = normalizarTexto(cor);
        this.veiculoClienteAno = ano;
        this.veiculoClienteKm = km;
        this.veiculoClienteObservacao = normalizarTexto(observacao);
    }

    private void validarNumeroOs(Long numeroOs) {
        if (numeroOs == null) {
            throw new IllegalArgumentException(
                    "O número da ordem de serviço é obrigatório."
            );
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException(
                    "O cliente da ordem de serviço é obrigatório."
            );
        }
    }

    private void validarTipoServico(TipoServico tipoServico) {
        if (tipoServico == null) {
            throw new IllegalArgumentException(
                    "O tipo de serviço é obrigatório."
            );
        }
    }

    private void validarOrigem(String origem) {
        if (origem == null || origem.isBlank()) {
            throw new IllegalArgumentException(
                    "A origem do atendimento é obrigatória."
            );
        }
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim();
    }

    private String normalizarTextoMaiusculo(String valor) {
        String texto = normalizarTexto(valor);

        return texto == null
                ? null
                : texto.toUpperCase();
    }
}