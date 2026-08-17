package br.com.lcdigitaltec.autoreboque_tora.domain.usuario;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.Perfil;
import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            length = 120
    )
    private String nome;

    @Column(
            nullable = false,
            unique = true,
            length = 160
    )
    private String email;

    @Column(
            name = "senha_hash",
            nullable = false
    )
    private String senhaHash;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "perfil_id",
            nullable = false
    )
    private Perfil perfil;

    public Usuario() {
    }

    public Usuario(
            String nome,
            String email,
            String senhaHash,
            Perfil perfil
    ) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.perfil = perfil;
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void alterarNome(String nome) {
        this.nome = nome;
    }

    public void alterarEmail(String email) {
        this.email = email;
    }

    public void alterarSenha(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void alterarPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (perfil == null || !perfil.isAtivo()) {
            return List.of();
        }

        return perfil
                .getPermissoes()
                .stream()
                .map(permissao ->
                        new SimpleGrantedAuthority(
                                permissao.getCodigo()
                        )
                )
                .toList();
    }

    @Override
    public String getPassword() {
        return senhaHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return ativo
                && perfil != null
                && perfil.isAtivo();
    }
}