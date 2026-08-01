package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServico;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServicoRepository;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LancamentoFinanceiroService {

    private final LancamentoFinanceiroRepository lancamentoFinanceiroRepository;
    private final VeiculoRepository veiculoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public LancamentoFinanceiroService(
            LancamentoFinanceiroRepository lancamentoFinanceiroRepository,
            VeiculoRepository veiculoRepository,
            OrdemServicoRepository ordemServicoRepository
    ) {
        this.lancamentoFinanceiroRepository = lancamentoFinanceiroRepository;
        this.veiculoRepository = veiculoRepository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoFinanceiroResponse> listarTodos() {
        return lancamentoFinanceiroRepository.findAll()
                .stream()
                .map(LancamentoFinanceiroResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public LancamentoFinanceiroResponse buscarPorId(Long id) {
        LancamentoFinanceiro lancamento = buscarEntidadePorId(id);
        return new LancamentoFinanceiroResponse(lancamento);
    }

    @Transactional
    public LancamentoFinanceiroResponse cadastrar(LancamentoFinanceiroRequest request) {
        Veiculo veiculo = buscarVeiculoOpcional(request.veiculoId());
        OrdemServico ordemServico = buscarOrdemServicoOpcional(request.ordemServicoId());

        LancamentoFinanceiro lancamento = new LancamentoFinanceiro(
                veiculo,
                ordemServico,
                request.tipo(),
                request.categoria(),
                request.status(),
                request.descricao(),
                request.valor(),
                request.dataLancamento(),
                request.observacao()
        );

        LancamentoFinanceiro salvo = lancamentoFinanceiroRepository.save(lancamento);

        return new LancamentoFinanceiroResponse(salvo);
    }

    @Transactional
    public LancamentoFinanceiroResponse atualizar(Long id, LancamentoFinanceiroRequest request) {
        LancamentoFinanceiro lancamento = buscarEntidadePorId(id);

        if (lancamento.getStatus() == StatusPagamento.CANCELADO) {
            throw new RegraNegocioException("Lançamento cancelado não pode ser alterado");
        }

        Veiculo veiculo = buscarVeiculoOpcional(request.veiculoId());
        OrdemServico ordemServico = buscarOrdemServicoOpcional(request.ordemServicoId());

        StatusPagamento status = request.status() == null
                ? lancamento.getStatus()
                : request.status();

        lancamento.atualizar(
                veiculo,
                ordemServico,
                request.tipo(),
                request.categoria(),
                status,
                request.descricao(),
                request.valor(),
                request.dataLancamento(),
                request.observacao()
        );

        return new LancamentoFinanceiroResponse(lancamento);
    }

    @Transactional
    public LancamentoFinanceiroResponse marcarComoPago(Long id) {
        LancamentoFinanceiro lancamento = buscarEntidadePorId(id);

        if (lancamento.getStatus() == StatusPagamento.CANCELADO) {
            throw new RegraNegocioException("Lançamento cancelado não pode ser marcado como pago");
        }

        if (lancamento.getStatus() == StatusPagamento.PAGO) {
            throw new RegraNegocioException("Lançamento já está pago");
        }

        lancamento.marcarComoPago();

        return new LancamentoFinanceiroResponse(lancamento);
    }

    @Transactional
    public LancamentoFinanceiroResponse cancelar(Long id) {
        LancamentoFinanceiro lancamento = buscarEntidadePorId(id);

        if (lancamento.getStatus() == StatusPagamento.CANCELADO) {
            throw new RegraNegocioException("Lançamento já está cancelado");
        }

        lancamento.cancelar();

        return new LancamentoFinanceiroResponse(lancamento);
    }

    @Transactional
    public void deletar(Long id) {
        LancamentoFinanceiro lancamento = buscarEntidadePorId(id);

        if (lancamento.getStatus() == StatusPagamento.PAGO) {
            throw new RegraNegocioException("Lançamento pago não pode ser excluído. Cancele o lançamento.");
        }

        lancamentoFinanceiroRepository.delete(lancamento);
    }

    @Transactional(readOnly = true)
    public ResumoFinanceiroResponse resumo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("Data inicial não pode ser maior que a data final");
        }

        var receitas = lancamentoFinanceiroRepository.somarPorTipoEPeriodo(
                TIpoLancamento.RECEITA,
                inicio,
                fim
        );

        var despesas = lancamentoFinanceiroRepository.somarPorTipoEPeriodo(
                TIpoLancamento.DESPESA,
                inicio,
                fim
        );

        var lucroBruto = receitas.subtract(despesas);

        return new ResumoFinanceiroResponse(
                inicio,
                fim,
                receitas,
                despesas,
                lucroBruto
        );
    }

    @Transactional
    public void registrarReceitaDaOrdemServico(OrdemServico ordemServico) {
        if (ordemServico.getValorCobrado() == null || ordemServico.getValorCobrado().signum() <= 0) {
            return;
        }

        boolean jaExisteReceita = lancamentoFinanceiroRepository.existsByOrdemServicoIdAndTipo(
                ordemServico.getId(),
                TIpoLancamento.RECEITA
        );

        if (jaExisteReceita) {
            return;
        }

        LancamentoFinanceiro lancamento = new LancamentoFinanceiro(
                ordemServico.getVeiculo(),
                ordemServico,
                TIpoLancamento.RECEITA,
                CategoriaFinanceira.SERVICO_GUINCHO,
                StatusPagamento.PENDENTE,
                "Receita da ordem de serviço #" + ordemServico.getId(),
                ordemServico.getValorCobrado(),
                LocalDate.now(),
                "Lançamento gerado automaticamente ao faturar a OS"
        );

        lancamentoFinanceiroRepository.save(lancamento);
    }

    @Transactional
    public void registrarDespesaDiesel(
            Veiculo veiculo,
            BigDecimal valorTotal,
            LocalDate dataLancamento,
            String observacao
    ) {
        if (valorTotal == null || valorTotal.signum() <= 0) {
            return;
        }

        LancamentoFinanceiro lancamento = new LancamentoFinanceiro(
                veiculo,
                null,
                TIpoLancamento.DESPESA,
                CategoriaFinanceira.DIESEL,
                StatusPagamento.PAGO,
                "Abastecimento de diesel - veículo " + veiculo.getPlaca(),
                valorTotal,
                dataLancamento,
                observacao
        );

        lancamentoFinanceiroRepository.save(lancamento);
    }

    @Transactional
    public void registrarDespesaManutencao(
            Veiculo veiculo,
            BigDecimal valorTotal,
            LocalDate dataLancamento,
            String observacao
    ) {
        if (valorTotal == null || valorTotal.signum() <= 0) {
            return;
        }

        LancamentoFinanceiro lancamento = new LancamentoFinanceiro(
                veiculo,
                null,
                TIpoLancamento.DESPESA,
                CategoriaFinanceira.MANUTENCAO,
                StatusPagamento.PAGO,
                "Despesa de manutenção - veículo " + veiculo.getPlaca(),
                valorTotal,
                dataLancamento,
                observacao
        );

        lancamentoFinanceiroRepository.save(lancamento);
    }

    @Transactional
    public void registrarDespesaDocumentacao(
            Veiculo veiculo,
            BigDecimal valorTotal,
            LocalDate dataLancamento,
            String observacao
    ) {
        if (valorTotal == null || valorTotal.signum() <= 0) {
            return;
        }

        LancamentoFinanceiro lancamento = new LancamentoFinanceiro(
                veiculo,
                null,
                TIpoLancamento.DESPESA,
                CategoriaFinanceira.DOCUMENTACAO,
                StatusPagamento.PAGO,
                "Despesa de documentação - veículo " + veiculo.getPlaca(),
                valorTotal,
                dataLancamento,
                observacao
        );

        lancamentoFinanceiroRepository.save(lancamento);
    }

    private LancamentoFinanceiro buscarEntidadePorId(Long id) {
        return lancamentoFinanceiroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lançamento financeiro não encontrado"));
    }

    private Veiculo buscarVeiculoOpcional(Long veiculoId) {
        if (veiculoId == null) {
            return null;
        }

        return veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }

    private OrdemServico buscarOrdemServicoOpcional(Long ordemServicoId) {
        if (ordemServicoId == null) {
            return null;
        }

        return ordemServicoRepository.findById(ordemServicoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
    }
}