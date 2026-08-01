package br.com.lcdigitaltec.autoreboque_tora.veiculo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String placa;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    private Integer ano;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeiculo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVeiculo status;

    @Column(name = "km_atual", nullable = false)
    private BigDecimal kmAtual = BigDecimal.ZERO;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected Veiculo() {
    }

    public Veiculo(
            String placa,
            String marca,
            String modelo,
            Integer ano,
            TipoVeiculo tipo,
            BigDecimal kmAtual,
            String observacao
    ) {
        this.placa = placa.toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.tipo = tipo;
        this.status = StatusVeiculo.DISPONIVEL;
        this.kmAtual = kmAtual == null ? BigDecimal.ZERO : kmAtual;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Integer getAno() {
        return ano;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public StatusVeiculo getStatus() {
        return status;
    }

    public BigDecimal getKmAtual() {
        return kmAtual;
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
            String placa,
            String marca,
            String modelo,
            Integer ano,
            TipoVeiculo tipo,
            StatusVeiculo status,
            BigDecimal kmAtual,
            String observacao
    ) {
        this.placa = placa.toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.ano = ano;
        this.tipo = tipo;
        this.status = status;
        this.kmAtual = kmAtual == null ? BigDecimal.ZERO : kmAtual;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarStatus(StatusVeiculo status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }
    public void atualizarKmAtual(BigDecimal novoKmAtual) {
        if (novoKmAtual == null) {
            return;
        }

        if (this.kmAtual == null || novoKmAtual.compareTo(this.kmAtual) > 0) {
            this.kmAtual = novoKmAtual;
            this.atualizadoEm = LocalDateTime.now();
        }
    }
}
