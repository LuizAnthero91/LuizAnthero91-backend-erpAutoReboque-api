package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroService;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.StatusVeiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManutencaoService {

    private final ManutencaoRepository manutencaoRepository;
    private final VeiculoRepository veiculoRepository;
    private final LancamentoFinanceiroService lancamentoFinanceiroService;

    public ManutencaoService(
            ManutencaoRepository manutencaoRepository,
            VeiculoRepository veiculoRepository,
            LancamentoFinanceiroService lancamentoFinanceiroService
    ) {
        this.manutencaoRepository = manutencaoRepository;
        this.veiculoRepository = veiculoRepository;
        this.lancamentoFinanceiroService = lancamentoFinanceiroService;
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarTodos() {
        return manutencaoRepository.findAll()
                .stream()
                .map(ManutencaoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ManutencaoResponse buscarPorId(Long id) {
        Manutencao manutencao = buscarEntidadePorId(id);
        return new ManutencaoResponse(manutencao);
    }

    @Transactional(readOnly = true)
    public List<ManutencaoResponse> listarPorVeiculo(Long veiculoId) {
        return manutencaoRepository.findByVeiculoId(veiculoId)
                .stream()
                .map(ManutencaoResponse::new)
                .toList();
    }

    @Transactional
    public ManutencaoResponse cadastrar(ManutencaoRequest request) {
        Veiculo veiculo = buscarVeiculo(request.veiculoId());

        Manutencao manutencao = new Manutencao(
                veiculo,
                request.tipo(),
                request.dataManutencao(),
                request.kmAtual(),
                request.descricao(),
                request.oficina(),
                request.custoPecas(),
                request.custoMaoObra(),
                request.proximaManutencaoKm(),
                request.proximaManutencaoData(),
                request.observacao()
        );

        Manutencao salva = manutencaoRepository.save(manutencao);

        veiculo.atualizarKmAtual(request.kmAtual());

        return new ManutencaoResponse(salva);
    }

    @Transactional
    public ManutencaoResponse atualizar(Long id, ManutencaoRequest request) {
        Manutencao manutencao = buscarEntidadePorId(id);

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new RegraNegocioException("Manutenção concluída não pode ser alterada");
        }

        if (manutencao.getStatus() == StatusManutencao.CANCELADA) {
            throw new RegraNegocioException("Manutenção cancelada não pode ser alterada");
        }

        StatusManutencao status = request.status() == null
                ? manutencao.getStatus()
                : request.status();

        manutencao.atualizar(
                request.tipo(),
                status,
                request.dataManutencao(),
                request.kmAtual(),
                request.descricao(),
                request.oficina(),
                request.custoPecas(),
                request.custoMaoObra(),
                request.proximaManutencaoKm(),
                request.proximaManutencaoData(),
                request.observacao()
        );

        manutencao.getVeiculo().atualizarKmAtual(request.kmAtual());

        return new ManutencaoResponse(manutencao);
    }

    @Transactional
    public ManutencaoResponse iniciar(Long id) {
        Manutencao manutencao = buscarEntidadePorId(id);

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new RegraNegocioException("Manutenção já concluída");
        }

        if (manutencao.getStatus() == StatusManutencao.CANCELADA) {
            throw new RegraNegocioException("Manutenção cancelada não pode ser iniciada");
        }

        if (manutencao.getStatus() == StatusManutencao.EM_ANDAMENTO) {
            throw new RegraNegocioException("Manutenção já está em andamento");
        }

        manutencao.iniciar();
        manutencao.getVeiculo().alterarStatus(StatusVeiculo.EM_MANUTENCAO);

        return new ManutencaoResponse(manutencao);
    }

    @Transactional
    public ManutencaoResponse concluir(Long id) {
        Manutencao manutencao = buscarEntidadePorId(id);

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new RegraNegocioException("Manutenção já concluída");
        }

        if (manutencao.getStatus() == StatusManutencao.CANCELADA) {
            throw new RegraNegocioException("Manutenção cancelada não pode ser concluída");
        }

        if (manutencao.getStatus() != StatusManutencao.EM_ANDAMENTO) {
            throw new RegraNegocioException("Somente manutenção em andamento pode ser concluída");
        }

        manutencao.concluir();
        manutencao.getVeiculo().alterarStatus(StatusVeiculo.DISPONIVEL);

        lancamentoFinanceiroService.registrarDespesaManutencao(
                manutencao.getVeiculo(),
                manutencao.getCustoTotal(),
                manutencao.getDataManutencao(),
                "Despesa gerada pela manutenção #" + manutencao.getId()
        );

        return new ManutencaoResponse(manutencao);
    }

    @Transactional
    public ManutencaoResponse cancelar(Long id) {
        Manutencao manutencao = buscarEntidadePorId(id);

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new RegraNegocioException("Manutenção concluída não pode ser cancelada");
        }

        if (manutencao.getStatus() == StatusManutencao.CANCELADA) {
            throw new RegraNegocioException("Manutenção já está cancelada");
        }

        manutencao.cancelar();
        manutencao.getVeiculo().alterarStatus(StatusVeiculo.DISPONIVEL);

        return new ManutencaoResponse(manutencao);
    }

    @Transactional
    public void deletar(Long id) {
        Manutencao manutencao = buscarEntidadePorId(id);

        if (manutencao.getStatus() == StatusManutencao.CONCLUIDA) {
            throw new RegraNegocioException("Manutenção concluída não pode ser excluída");
        }

        manutencaoRepository.delete(manutencao);
    }

    private Manutencao buscarEntidadePorId(Long id) {
        return manutencaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Manutenção não encontrada"));
    }

    private Veiculo buscarVeiculo(Long veiculoId) {
        return veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }
}