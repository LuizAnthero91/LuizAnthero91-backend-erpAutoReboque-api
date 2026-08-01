package br.com.lcdigitaltec.autoreboque_tora.cliente;

import java.time.LocalDateTime;

public record ClienteResponse(
        Long id,
        String nome,
        String documento,
        String telefone,
        String email,
        TipoCliente tipo,
        StatusCliente status,
        String endereco,
        String observacao,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {

    public ClienteResponse(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getDocumento(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getTipo(),
                cliente.getStatus(),
                cliente.getEndereco(),
                cliente.getObservacao(),
                cliente.getCriadoEm(),
                cliente.getAtualizadoEm()
        );
    }
}