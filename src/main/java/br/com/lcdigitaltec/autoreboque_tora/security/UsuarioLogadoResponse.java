package br.com.lcdigitaltec.autoreboque_tora.security;

import java.util.List;

public record UsuarioLogadoResponse(
        Long id,
        String nome,
        String email,
        String perfil,
        List<String> permissoes
) {
}
