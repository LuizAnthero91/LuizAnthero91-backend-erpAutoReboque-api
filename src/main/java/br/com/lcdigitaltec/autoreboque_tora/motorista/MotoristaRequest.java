package br.com.lcdigitaltec.autoreboque_tora.motorista;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record MotoristaRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String cpf,

        String telefone,

        String cnh,

        String categoriaCnh,

        LocalDate validadeCnh,

        StatusMotorista status,

        String observacao
) {
}