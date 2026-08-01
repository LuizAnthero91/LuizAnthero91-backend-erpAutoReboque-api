package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentoVeiculoResponse(
        Long id,

        Long veiculoId,
        String veiculoPlaca,
        String veiculoModelo,

        TipoDocumentoVeiculo tipo,
        StatusDocumentoVeiculo status,

        String numeroDocumento,
        LocalDate dataEmissao,
        LocalDate dataVencimento,

        BigDecimal valor,

        String orgaoEmissor,
        String arquivoUrl,

        boolean despesaGerada,

        String observacao,

        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public DocumentoVeiculoResponse(DocumentoVeiculo documento) {
        this(
                documento.getId(),

                documento.getVeiculo().getId(),
                documento.getVeiculo().getPlaca(),
                documento.getVeiculo().getModelo(),

                documento.getTipo(),
                documento.getStatus(),

                documento.getNumeroDocumento(),
                documento.getDataEmissao(),
                documento.getDataVencimento(),

                documento.getValor(),

                documento.getOrgaoEmissor(),
                documento.getArquivoUrl(),

                documento.isDespesaGerada(),

                documento.getObservacao(),

                documento.getCriadoEm(),
                documento.getAtualizadoEm()
        );
    }
}