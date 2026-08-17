package br.com.lcdigitaltec.autoreboque_tora.domain.perfil;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.Perfil;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository
        extends JpaRepository<Perfil, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(
            String nome,
            Long id
    );

    Optional<Perfil> findByNomeIgnoreCase(
            String nome
    );

    @Override
    @EntityGraph(attributePaths = "permissoes")
    Optional<Perfil> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "permissoes")
    List<Perfil> findAll();
}