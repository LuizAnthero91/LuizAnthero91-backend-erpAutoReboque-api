package br.com.lcdigitaltec.autoreboque_tora.security;

public record UsuarioLogadoResponse(
        Long id,
        String nome,
        String email,
        String perfil
) {
}
