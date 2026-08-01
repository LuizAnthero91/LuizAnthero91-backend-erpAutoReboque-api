package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrdemServicoResponse(
        Long id,

        Long clienteId,
        String clienteNome,

        Long veiculoId,
        String veiculoPlaca,
        String veiculoModelo,

        Long motoristaId,
        String motoristaNome,

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

                ordem.getCliente().getId(),
                ordem.getCliente().getNome(),

                ordem.getVeiculo() == null ? null : ordem.getVeiculo().getId(),
                ordem.getVeiculo() == null ? null : ordem.getVeiculo().getPlaca(),
                ordem.getVeiculo() == null ? null : ordem.getVeiculo().getModelo(),

                ordem.getMotorista() == null ? null : ordem.getMotorista().getId(),
                ordem.getMotorista() == null ? null : ordem.getMotorista().getNome(),

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
