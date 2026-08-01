package br.com.lcdigitaltec.autoreboque_tora.common.exception;

public record ErroCampoResponse(
        String campo,
        String mensagem
) {
}