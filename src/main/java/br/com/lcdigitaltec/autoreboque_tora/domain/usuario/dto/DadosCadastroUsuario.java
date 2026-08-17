package br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(

        @NotBlank(message = "O nome é obrigatório")
        @Size(
                min = 3,
                max = 120,
                message = "O nome deve possuir entre 3 e 120 caracteres"
        )
        String nome,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Informe um e-mail válido")
        @Size(
                max = 160,
                message = "O e-mail deve possuir no máximo 160 caracteres"
        )
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(
                min = 8,
                max = 100,
                message = "A senha deve possuir entre 8 e 100 caracteres"
        )
        String senha,

        @NotNull(message = "O perfil é obrigatório")
        @Positive(message = "O perfil informado é inválido")
        Long perfilId

) {
}