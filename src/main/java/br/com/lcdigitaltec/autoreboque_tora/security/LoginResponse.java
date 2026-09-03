package br.com.lcdigitaltec.autoreboque_tora.security;

public record LoginResponse(
        String token,
        String tipo,
        Long usuarioId,
        String nome,
        String email,
        String perfil
) {
}
