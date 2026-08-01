package br.com.lcdigitaltec.autoreboque_tora.dashboard;

import java.math.BigDecimal;

public record DashboardFinanceiroMensalResponse(
        int ano,
        int mes,
        String nomeMes,
        BigDecimal receitas,
        BigDecimal despesas,
        BigDecimal lucroBruto
) {
}