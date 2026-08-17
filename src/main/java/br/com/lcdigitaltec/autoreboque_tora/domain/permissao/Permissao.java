package br.com.lcdigitaltec.autoreboque_tora.domain.permissao;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String codigo;

    @Column(
            nullable = false,
            length = 255
    )
    private String descricao;

    public Permissao() {
    }

    public Permissao(
            String codigo,
            String descricao
    ) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void alterarDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Permissao permissao)) {
            return false;
        }

        return id != null &&
                Objects.equals(id, permissao.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}