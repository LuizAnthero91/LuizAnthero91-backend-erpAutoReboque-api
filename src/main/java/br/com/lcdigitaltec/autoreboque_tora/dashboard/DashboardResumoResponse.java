package br.com.lcdigitaltec.autoreboque_tora.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardResumoResponse(
        LocalDate inicioMes,
        LocalDate fimMes,

        long totalVeiculos,
        long veiculosDisponiveis,
        long veiculosEmAtendimento,
        long veiculosEmManutencao,
        long veiculosInativos,

        long totalMotoristas,
        long totalClientes,

        long ordensAbertas,
        long ordensAgendadas,
        long ordensEmAtendimento,
        long ordensConcluidas,
        long ordensFaturadas,
        long ordensCanceladas,

        long manutencoesAbertas,
        long manutencoesEmAndamento,
        long manutencoesConcluidas,

        long documentosVencidos,
        long documentosAVencer,

        BigDecimal receitaMes,
        BigDecimal despesaMes,
        BigDecimal lucroBrutoMes
) {
}