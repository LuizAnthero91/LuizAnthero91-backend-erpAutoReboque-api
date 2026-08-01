package br.com.lcdigitaltec.autoreboque_tora.auth;

public record LoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        String perfil
) {
}