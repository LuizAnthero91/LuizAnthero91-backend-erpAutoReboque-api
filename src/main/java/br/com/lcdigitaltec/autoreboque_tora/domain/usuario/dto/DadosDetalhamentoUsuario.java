package br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto;

import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.Usuario;

public record DadosDetalhamentoUsuario(

        Long id,
        String nome,
        String email,
        boolean ativo,
        Long perfilId,
        String perfilNome

) {

    public DadosDetalhamentoUsuario(
            Usuario usuario
    ) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.isAtivo(),
                usuario.getPerfil().getId(),
                usuario.getPerfil().getNome()
        );
    }
}