package br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record DadosCadastroPerfil(

        @NotBlank(
                message = "O nome do perfil é obrigatório"
        )
        @Size(
                max = 100,
                message = "O nome do perfil deve possuir no máximo 100 caracteres"
        )
        String nome,

        @Size(
                max = 255,
                message = "A descrição deve possuir no máximo 255 caracteres"
        )
        String descricao,

        @NotEmpty(
                message = "O perfil deve possuir pelo menos uma permissão"
        )
        Set<Long> permissoes

) {
}