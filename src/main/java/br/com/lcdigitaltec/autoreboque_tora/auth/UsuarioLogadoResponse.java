package br.com.lcdigitaltec.autoreboque_tora.auth;

public record UsuarioLogadoResponse(
        Long id,
        String nome,
        String email,
        String perfil
) {
}