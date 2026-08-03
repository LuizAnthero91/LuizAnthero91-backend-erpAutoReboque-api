package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoResponse(
        Long id,
        Long numeroOs,

        // demais campos já existentes
        Long clienteId,
        String clienteNome,
        Long veiculoId,
        Long motoristaId,
        TipoServico tipoServico,
        StatusOrdemServico status,
        String origem,
        String destino,
        BigDecimal kmEstimado,
        BigDecimal kmReal,
        BigDecimal valorCobrado,
        BigDecimal custoEstimado,
        LocalDateTime dataAbertura,
        LocalDateTime dataConclusao,
        String observacao
) {

    public OrdemServicoResponse(OrdemServico ordem) {
        this(
                ordem.getId(),
                ordem.getNumeroOs(),

                // mantenha aqui o restante do seu mapeamento atual
                ordem.getCliente().getId(),
                ordem.getCliente().getNome(),
                ordem.getVeiculo() == null
                        ? null
                        : ordem.getVeiculo().getId(),
                ordem.getMotorista() == null
                        ? null
                        : ordem.getMotorista().getId(),
                ordem.getTipoServico(),
                ordem.getStatus(),
                ordem.getOrigem(),
                ordem.getDestino(),
                ordem.getKmEstimado(),
                ordem.getKmReal(),
                ordem.getValorCobrado(),
                ordem.getCustoEstimado(),
                ordem.getDataAbertura(),
                ordem.getDataConclusao(),
                ordem.getObservacao()
        );
    }
}
