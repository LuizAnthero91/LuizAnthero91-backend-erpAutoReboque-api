package br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto;

import jakarta.validation.constraints.Size;

import java.util.Set;

public record DadosAtualizacaoPerfil(

        @Size(
                min = 2,
                max = 100,
                message = "O nome do perfil deve possuir entre 2 e 100 caracteres"
        )
        String nome,

        @Size(
                max = 255,
                message = "A descrição deve possuir no máximo 255 caracteres"
        )
        String descricao,

        Boolean ativo,

        Set<Long> permissoes

) {
}