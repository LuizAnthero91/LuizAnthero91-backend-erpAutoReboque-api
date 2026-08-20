package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrdemServicoRepository
        extends JpaRepository<OrdemServico, Long> {

    Optional<OrdemServico> findByNumeroOs(Long numeroOs);

    boolean existsByNumeroOs(Long numeroOs);

    List<OrdemServico> findByStatus(StatusOrdemServico status);

    List<OrdemServico> findByClienteId(Long clienteId);

    List<OrdemServico> findByVeiculoId(Long veiculoId);

    List<OrdemServico> findByMotoristaId(Long motoristaId);

    long countByStatus(StatusOrdemServico status);

    @Query(value = """
                   SELECT os
                   FROM OrdemServico os
                   JOIN FETCH os.cliente
                   LEFT JOIN FETCH os.veiculo
                   LEFT JOIN FETCH os.motorista
                   """,
           countQuery = """
           SELECT COUNT(os)
           FROM OrdemServico os
           """
    )
    Page<OrdemServico> findALLComRelacionamentos(Pageable pageable);

}