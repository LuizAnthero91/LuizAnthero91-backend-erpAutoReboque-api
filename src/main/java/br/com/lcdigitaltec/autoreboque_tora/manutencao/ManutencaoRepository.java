package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    List<Manutencao> findByVeiculoId(Long veiculoId);

    List<Manutencao> findByStatus(StatusManutencao status);

    long countByStatus(StatusManutencao status);
}
