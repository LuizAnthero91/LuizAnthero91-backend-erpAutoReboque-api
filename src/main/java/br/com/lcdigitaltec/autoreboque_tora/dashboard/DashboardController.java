package br.com.lcdigitaltec.autoreboque_tora.dashboard;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/resumo")
    public DashboardResumoResponse resumo() {
        return dashboardService.resumo();
    }

    @GetMapping("/financeiro-mensal")
    public List<br.com.lcdigitaltec.autoreboque_tora.dashboard.DashboardFinanceiroMensalResponse> financeiroMensal(
            @RequestParam(required = false) Integer ano
    ) {
        int anoConsulta = ano == null ? LocalDate.now().getYear() : ano;
        return dashboardService.financeiroMensal(anoConsulta);
    }
}
