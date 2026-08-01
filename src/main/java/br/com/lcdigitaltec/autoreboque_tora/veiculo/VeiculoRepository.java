package br.com.lcdigitaltec.autoreboque_tora.veiculo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlaca(String placa);

    boolean existsByPlaca(String placa);

    long countByStatus(StatusVeiculo status);
}