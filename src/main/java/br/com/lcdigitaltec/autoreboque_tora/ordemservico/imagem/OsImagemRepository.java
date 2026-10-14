package br.com.lcdigitaltec.autoreboque_tora.ordemservico.imagem;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OsImagemRepository
        extends JpaRepository<OsImagem, Long> {

    List<OsImagem> findByOrdemServicoId(
            Long ordemServicoId
    );

    long countByOrdemServicoId(
            Long ordemServicoId
    );

    Optional<OsImagem> findByIdAndOrdemServicoId(
            Long imagemId,
            Long ordemServicoId
    );
}