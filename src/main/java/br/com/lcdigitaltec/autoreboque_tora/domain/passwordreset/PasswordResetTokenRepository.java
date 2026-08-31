package br.com.lcdigitaltec.autoreboque_tora.domain.passwordreset;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken>
    findByTokenHashAndUtilizadoEmIsNull(
            String tokenHash
    );

    void deleteByUsuarioId(
            Long usuarioId
    );
}
