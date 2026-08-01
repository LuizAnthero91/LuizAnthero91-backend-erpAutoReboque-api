package br.com.lcdigitaltec.autoreboque_tora.cliente;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String documento;

    private String telefone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCliente tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCliente status;

    private String endereco;

    private String observacao;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm = LocalDateTime.now();

    protected Cliente() {
    }

    public Cliente(
            String nome,
            String documento,
            String telefone,
            String email,
            TipoCliente tipo,
            String endereco,
            String observacao
    ) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
        this.tipo = tipo;
        this.status = StatusCliente.ATIVO;
        this.endereco = endereco;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public StatusCliente getStatus() {
        return status;
    }

    public String getEndereco() {
        return endereco;
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
            String documento,
            String telefone,
            String email,
            TipoCliente tipo,
            StatusCliente status,
            String endereco,
            String observacao
    ) {
        this.nome = nome;
        this.documento = documento;
        this.telefone = telefone;
        this.email = email;
        this.tipo = tipo;
        this.status = status;
        this.endereco = endereco;
        this.observacao = observacao;
        this.atualizadoEm = LocalDateTime.now();
    }

    public void alterarStatus(StatusCliente status) {
        this.status = status;
        this.atualizadoEm = LocalDateTime.now();
    }
}