package br.com.lcdigitaltec.autoreboque_tora.domain.usuario;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository
        extends JpaRepository<Usuario, Long> {

    @EntityGraph(
            attributePaths = {
                    "perfil",
                    "perfil.permissoes"
            }
    )
    Optional<Usuario> findByEmail(
            String email
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    boolean existsByEmailIgnoreCaseAndIdNot(
            String email,
            Long id
    );
}