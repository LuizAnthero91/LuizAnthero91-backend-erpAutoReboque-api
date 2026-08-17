package br.com.lcdigitaltec.autoreboque_tora.domain.permissao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PermissaoRepository
        extends JpaRepository<Permissao, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    Optional<Permissao> findByCodigoIgnoreCase(String codigo);

    List<Permissao> findAllByOrderByCodigoAsc();
}