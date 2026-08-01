package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    List<OrdemServico> findByStatus(StatusOrdemServico status);

    List<OrdemServico> findByClienteId(Long clienteId);

    List<OrdemServico> findByVeiculoId(Long veiculoId);

    List<OrdemServico> findByMotoristaId(Long motoristaId);

    long countByStatus(StatusOrdemServico status);
}