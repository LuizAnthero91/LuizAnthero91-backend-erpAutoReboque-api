package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import java.math.BigDecimal;

public interface FinanceiroMensalProjection {

    Integer getMes();

    BigDecimal getReceitas();

    BigDecimal getDespesas();
}