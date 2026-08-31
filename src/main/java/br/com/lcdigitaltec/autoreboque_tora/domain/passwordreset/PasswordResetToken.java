package br.com.lcdigitaltec.autoreboque_tora.domain.passwordreset;

import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.Usuario;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @Column(
            name = "token_hash",
            nullable = false,
            unique = true,
            length = 64
    )
    private String tokenHash;

    @Column(
            name = "criado_em",
            nullable = false
    )
    private Instant criadoEm;

    @Column(
            name = "expira_em",
            nullable = false
    )
    private Instant expiraEm;

    @Column(
            name = "utilizado_em"
    )
    private Instant utilizadoEm;

    public PasswordResetToken() {
    }

    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Instant getExpiraEm() {
        return expiraEm;
    }

    public void setExpiraEm(Instant expiraEm) {
        this.expiraEm = expiraEm;
    }

    public Instant getUtilizadoEm() {
        return utilizadoEm;
    }

    public void setUtilizadoEm(Instant utilizadoEm) {
        this.utilizadoEm = utilizadoEm;
    }
}