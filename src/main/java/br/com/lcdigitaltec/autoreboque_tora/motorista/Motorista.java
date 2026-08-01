package br.com.lcdigitaltec.autoreboque_tora.motorista;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "motoristas")
public class Motorista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String cpf;

    private String telefone;

    @Column(unique = true)
    private String cnh;

    @Column(name = "categoria_cnh")
    private String categoriaCnh;

    @Column(name = "validade_cnh")
    private LocalDate validadeCnh;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMotorista status;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected Motorista() {
    }

    public Motorista(
            String nome,
            String cpf,
            String telefone,
            String cnh,
            String categoriaCnh,
            LocalDate validadeCnh,
            String observacao
    ) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cnh = cnh;
        this.categoriaCnh = categoriaCnh;
        this.validadeCnh = validadeCnh;
        this.status = StatusMotorista.ATIVO;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCnh() {
        return cnh;
    }

    public String getCategoriaCnh() {
        return categoriaCnh;
    }

    public LocalDate getValidadeCnh() {
        return validadeCnh;
    }

    public StatusMotorista getStatus() {
        return status;
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
            String nome,
            String cpf,
            String telefone,
            String cnh,
            String categoriaCnh,
            LocalDate validadeCnh,
            StatusMotorista status,
            String observacao
    ) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.cnh = cnh;
        this.categoriaCnh = categoriaCnh;
        this.validadeCnh = validadeCnh;
        this.status = status;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarStatus(StatusMotorista status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }
}