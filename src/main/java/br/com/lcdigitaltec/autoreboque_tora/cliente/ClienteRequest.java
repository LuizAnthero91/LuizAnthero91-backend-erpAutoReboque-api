package br.com.lcdigitaltec.autoreboque_tora.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        String documento,

        String telefone,

        @Email(message = "Email inválido")
        String email,

        @NotNull(message = "Tipo do cliente é obrigatório")
        TipoCliente tipo,

        StatusCliente status,

        String endereco,

        String observacao
) {
}
