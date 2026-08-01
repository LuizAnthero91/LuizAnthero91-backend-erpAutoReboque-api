package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DocumentoVeiculoRequest(

        @NotNull(message = "Veículo é obrigatório")
        Long veiculoId,

        @NotNull(message = "Tipo do documento é obrigatório")
        TipoDocumentoVeiculo tipo,

        StatusDocumentoVeiculo status,

        String numeroDocumento,

        LocalDate dataEmissao,

        @NotNull(message = "Data de vencimento é obrigatória")
        LocalDate dataVencimento,

        @PositiveOrZero(message = "Valor não pode ser negativo")
        BigDecimal valor,

        String orgaoEmissor,

        String arquivoUrl,

        String observacao
) {
}
