package br.com.lcdigitaltec.autoreboque_tora.domain.permissao.dto;

import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.Permissao;

public record DadosDetalhamentoPermissao(

        Long id,
        String codigo,
        String descricao

) {

    public DadosDetalhamentoPermissao(
            Permissao permissao
    ) {
        this(
                permissao.getId(),
                permissao.getCodigo(),
                permissao.getDescricao()
        );
    }
}