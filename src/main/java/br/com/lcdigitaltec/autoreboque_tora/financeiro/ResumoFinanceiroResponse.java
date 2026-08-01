package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ResumoFinanceiroResponse(
        LocalDate inicio,
        LocalDate fim,
        BigDecimal receitas,
        BigDecimal despesas,
        BigDecimal lucroBruto
) {
}