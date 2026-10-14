package br.com.lcdigitaltec.autoreboque_tora.ordemservico.imagem;

import java.time.LocalDateTime;

public record OsImagemResponse(
        Long id,
        String url,
        String nomeOriginal,
        String contentType,
        Long tamanhoBytes,
        LocalDateTime criadoEm
) {

    public static OsImagemResponse from(
            OsImagem imagem
    ) {

        return new OsImagemResponse(
                imagem.getId(),
                imagem.getUrl(),
                imagem.getNomeOriginal(),
                imagem.getContentType(),
                imagem.getTamanhoBytes(),
                imagem.getCriadoEm()
        );
    }
}