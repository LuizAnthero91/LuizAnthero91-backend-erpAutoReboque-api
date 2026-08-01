package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentoVeiculoRepository extends JpaRepository<DocumentoVeiculo, Long> {

    List<DocumentoVeiculo> findAllByOrderByDataVencimentoAsc();

    List<DocumentoVeiculo> findByVeiculoIdOrderByDataVencimentoAsc(Long veiculoId);

    List<DocumentoVeiculo> findByStatusOrderByDataVencimentoAsc(StatusDocumentoVeiculo status);

    long countByStatus(StatusDocumentoVeiculo status);
}
