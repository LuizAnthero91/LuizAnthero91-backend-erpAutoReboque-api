package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import br.com.lcdigitaltec.autoreboque_tora.motorista.Motorista;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "abastecimentos")
public class Abastecimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne
    @JoinColumn(name = "motorista_id")
    private Motorista motorista;

    @Column(name = "data_abastecimento", nullable = false)
    private LocalDate dataAbastecimento;

    @Column(name = "km_atual", nullable = false)
    private BigDecimal kmAtual;

    @Column(nullable = false)
    private BigDecimal litros;

    @Column(name = "valor_litro", nullable = false)
    private BigDecimal valorLitro;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    private String posto;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected Abastecimento() {
    }

    public Abastecimento(
            Veiculo veiculo,
            Motorista motorista,
            LocalDate dataAbastecimento,
            BigDecimal kmAtual,
            BigDecimal litros,
            BigDecimal valorLitro,
            BigDecimal valorTotal,
            String posto,
            String observacao
    ) {
        this.veiculo = veiculo;
        this.motorista = motorista;
        this.dataAbastecimento = dataAbastecimento;
        this.kmAtual = kmAtual;
        this.litros = litros;
        this.valorLitro = valorLitro;
        this.valorTotal = valorTotal;
        this.posto = posto;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public LocalDate getDataAbastecimento() {
        return dataAbastecimento;
    }

    public BigDecimal getKmAtual() {
        return kmAtual;
    }

    public BigDecimal getLitros() {
        return litros;
    }

    public BigDecimal getValorLitro() {
        return valorLitro;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public String getPosto() {
        return posto;
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
}
