package br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.Perfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.dto.DadosDetalhamentoPermissao;
import java.util.Set;
import java.util.stream.Collectors;

public record DadosDetalhamentoPerfil(

        Long id,
        String nome,
        String descricao,
        boolean ativo,
        Set<DadosDetalhamentoPermissao> permissoes

) {

    public DadosDetalhamentoPerfil(
            Perfil perfil
    ) {
        this(
                perfil.getId(),
                perfil.getNome(),
                perfil.getDescricao(),
                perfil.isAtivo(),

                perfil.getPermissoes()
                        .stream()
                        .map(DadosDetalhamentoPermissao::new)
                        .collect(Collectors.toSet())
        );
    }
}