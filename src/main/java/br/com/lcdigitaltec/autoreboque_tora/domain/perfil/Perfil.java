package br.com.lcdigitaltec.autoreboque_tora.domain.perfil;

import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.Permissao;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "perfis")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_permissoes",
            joinColumns =
            @JoinColumn(name = "perfil_id"),
            inverseJoinColumns =
            @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    public Perfil() {
    }

    public Perfil(
            String nome,
            String descricao,
            Set<Permissao> permissoes
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.permissoes = permissoes;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Set<Permissao> getPermissoes() {
        return permissoes;
    }

    public void alterarNome(String nome) {
        this.nome = nome;
    }

    public void alterarDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void alterarPermissoes(
            Set<Permissao> permissoes
    ) {
        this.permissoes = permissoes;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}