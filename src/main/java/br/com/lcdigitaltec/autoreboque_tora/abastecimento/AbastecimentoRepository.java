package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {

    List<Abastecimento> findByVeiculoId(Long veiculoId);

    List<Abastecimento> findByMotoristaId(Long motoristaId);
}
