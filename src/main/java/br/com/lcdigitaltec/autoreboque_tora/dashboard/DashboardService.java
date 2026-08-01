package br.com.lcdigitaltec.autoreboque_tora.dashboard;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.FinanceiroMensalProjection;
import java.math.BigDecimal;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import br.com.lcdigitaltec.autoreboque_tora.cliente.ClienteRepository;
import br.com.lcdigitaltec.autoreboque_tora.documentoveiculo.DocumentoVeiculoRepository;
import br.com.lcdigitaltec.autoreboque_tora.documentoveiculo.StatusDocumentoVeiculo;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroRepository;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.TIpoLancamento;
import br.com.lcdigitaltec.autoreboque_tora.manutencao.ManutencaoRepository;
import br.com.lcdigitaltec.autoreboque_tora.manutencao.StatusManutencao;
import br.com.lcdigitaltec.autoreboque_tora.motorista.MotoristaRepository;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServicoRepository;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.StatusOrdemServico;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.StatusVeiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final VeiculoRepository veiculoRepository;
    private final MotoristaRepository motoristaRepository;
    private final ClienteRepository clienteRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final LancamentoFinanceiroRepository lancamentoFinanceiroRepository;
    private final DocumentoVeiculoRepository documentoVeiculoRepository;
    private final ManutencaoRepository manutencaoRepository;

    public DashboardService(
            VeiculoRepository veiculoRepository,
            MotoristaRepository motoristaRepository,
            ClienteRepository clienteRepository,
            OrdemServicoRepository ordemServicoRepository,
            LancamentoFinanceiroRepository lancamentoFinanceiroRepository,
            DocumentoVeiculoRepository documentoVeiculoRepository,
            ManutencaoRepository manutencaoRepository
    ) {
        this.veiculoRepository = veiculoRepository;
        this.motoristaRepository = motoristaRepository;
        this.clienteRepository = clienteRepository;
        this.ordemServicoRepository = ordemServicoRepository;
        this.lancamentoFinanceiroRepository = lancamentoFinanceiroRepository;
        this.documentoVeiculoRepository = documentoVeiculoRepository;
        this.manutencaoRepository = manutencaoRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResumoResponse resumo() {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioMes = hoje.withDayOfMonth(1);
        LocalDate fimMes = hoje.withDayOfMonth(hoje.lengthOfMonth());

        var receitaMes = lancamentoFinanceiroRepository.somarPorTipoEPeriodo(
                TIpoLancamento.RECEITA,
                inicioMes,
                fimMes
        );

        var despesaMes = lancamentoFinanceiroRepository.somarPorTipoEPeriodo(
                TIpoLancamento.DESPESA,
                inicioMes,
                fimMes
        );

        var lucroBrutoMes = receitaMes.subtract(despesaMes);

        return new DashboardResumoResponse(
                inicioMes,
                fimMes,

                veiculoRepository.count(),
                veiculoRepository.countByStatus(StatusVeiculo.DISPONIVEL),
                veiculoRepository.countByStatus(StatusVeiculo.EM_ATENDIMENTO),
                veiculoRepository.countByStatus(StatusVeiculo.EM_MANUTENCAO),
                veiculoRepository.countByStatus(StatusVeiculo.INATIVO),

                motoristaRepository.count(),
                clienteRepository.count(),

                ordemServicoRepository.countByStatus(StatusOrdemServico.ABERTA),
                ordemServicoRepository.countByStatus(StatusOrdemServico.AGENDADA),
                ordemServicoRepository.countByStatus(StatusOrdemServico.EM_ATENDIMENTO),
                ordemServicoRepository.countByStatus(StatusOrdemServico.CONCLUIDA),
                ordemServicoRepository.countByStatus(StatusOrdemServico.FATURADA),
                ordemServicoRepository.countByStatus(StatusOrdemServico.CANCELADA),

                manutencaoRepository.countByStatus(StatusManutencao.ABERTA),
                manutencaoRepository.countByStatus(StatusManutencao.EM_ANDAMENTO),
                manutencaoRepository.countByStatus(StatusManutencao.CONCLUIDA),

                documentoVeiculoRepository.countByStatus(StatusDocumentoVeiculo.VENCIDO),
                documentoVeiculoRepository.countByStatus(StatusDocumentoVeiculo.A_VENCER),

                receitaMes,
                despesaMes,
                lucroBrutoMes
        );
    }

    @Transactional(readOnly = true)
    public List<DashboardFinanceiroMensalResponse> financeiroMensal(int ano) {
        List<FinanceiroMensalProjection> dados = lancamentoFinanceiroRepository.buscarFinanceiroMensal(ano);

        Map<Integer, FinanceiroMensalProjection> dadosPorMes = dados.stream()
                .collect(Collectors.toMap(
                        FinanceiroMensalProjection::getMes,
                        Function.identity()
                ));

        return IntStream.rangeClosed(1, 12)
                .mapToObj(mes -> {
                    FinanceiroMensalProjection item = dadosPorMes.get(mes);

                    BigDecimal receitas = item == null ? BigDecimal.ZERO : valorOuZero(item.getReceitas());
                    BigDecimal despesas = item == null ? BigDecimal.ZERO : valorOuZero(item.getDespesas());
                    BigDecimal lucroBruto = receitas.subtract(despesas);

                    return new DashboardFinanceiroMensalResponse(
                            ano,
                            mes,
                            nomeMes(mes),
                            receitas,
                            despesas,
                            lucroBruto
                    );
                })
                .toList();
    }

    private BigDecimal valorOuZero(BigDecimal valor) {
        return valor == null ? BigDecimal.ZERO : valor;
    }

    private String nomeMes(int mes) {
        return Month.of(mes)
                .getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("pt-BR"));
    }
}
