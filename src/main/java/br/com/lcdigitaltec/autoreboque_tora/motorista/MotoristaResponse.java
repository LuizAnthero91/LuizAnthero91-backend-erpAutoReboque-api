package br.com.lcdigitaltec.autoreboque_tora.motorista;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MotoristaResponse(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String cnh,
        String categoriaCnh,
        LocalDate validadeCnh,
        StatusMotorista status,
        String observacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public MotoristaResponse(Motorista motorista) {
        this(
                motorista.getId(),
                motorista.getNome(),
                motorista.getCpf(),
                motorista.getTelefone(),
                motorista.getCnh(),
                motorista.getCategoriaCnh(),
                motorista.getValidadeCnh(),
                motorista.getStatus(),
                motorista.getObservacao(),
                motorista.getCriadoEm(),
                motorista.getAtualizadoEm()
        );
    }
}
