package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import br.com.lcdigitaltec.autoreboque_tora.cliente.Cliente;
import br.com.lcdigitaltec.autoreboque_tora.cliente.ClienteRepository;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroService;
import br.com.lcdigitaltec.autoreboque_tora.motorista.Motorista;
import br.com.lcdigitaltec.autoreboque_tora.motorista.MotoristaRepository;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final MotoristaRepository motoristaRepository;
    private final LancamentoFinanceiroService lancamentoFinanceiroService;
    private final NumeroOrdemServicoService numeroOrdemServicoService;

    public OrdemServicoService(
            OrdemServicoRepository ordemServicoRepository,
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            MotoristaRepository motoristaRepository,
            LancamentoFinanceiroService lancamentoFinanceiroService,
            NumeroOrdemServicoService numeroOrdemServicoService
    ) {
        this.ordemServicoRepository = ordemServicoRepository;
        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.motoristaRepository = motoristaRepository;
        this.lancamentoFinanceiroService = lancamentoFinanceiroService;
        this.numeroOrdemServicoService = numeroOrdemServicoService;
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponse> listarTodos() {
        return ordemServicoRepository.findAll()
                .stream()
                .map(OrdemServicoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse buscarPorId(Long id) {
        OrdemServico ordem = buscarEntidadePorId(id);
        return new OrdemServicoResponse(ordem);
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponse buscarPorNumero(Long numeroOs) {
        OrdemServico ordem = ordemServicoRepository
                .findByNumeroOs(numeroOs)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException(
                                "Ordem de serviço não encontrada."
                        )
                );

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse cadastrar(OrdemServicoRequest request) {
        Cliente cliente = buscarCliente(request.clienteId());
        Veiculo veiculo = buscarVeiculoOpcional(request.veiculoId());
        Motorista motorista = buscarMotoristaOpcional(request.motoristaId());

        Long numeroOs = numeroOrdemServicoService.gerarProximoNumero();

        OrdemServico ordem = new OrdemServico(
                numeroOs,
                cliente,
                veiculo,
                motorista,
                request.tipoServico(),
                request.origem(),
                request.destino(),
                request.kmEstimado(),
                request.valorCobrado(),
                request.custoEstimado(),
                request.observacao(),

                request.veiculoClientePlaca(),
                request.veiculoClienteMarca(),
                request.veiculoClienteModelo(),
                request.veiculoClienteCor(),
                request.veiculoClienteAno(),
                request.veiculoClienteKm(),
                request.veiculoClienteObservacao()
        );

        OrdemServico salva = ordemServicoRepository.save(ordem);

        return new OrdemServicoResponse(salva);
    }

    @Transactional
    public OrdemServicoResponse atualizar(Long id, OrdemServicoRequest request) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço faturada não pode ser alterada");
        }

        if (ordem.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Ordem de serviço cancelada não pode ser alterada");
        }

        Cliente cliente = buscarCliente(request.clienteId());
        Veiculo veiculo = buscarVeiculoOpcional(request.veiculoId());
        Motorista motorista = buscarMotoristaOpcional(request.motoristaId());

        StatusOrdemServico status = request.status() == null
                ? ordem.getStatus()
                : request.status();

        ordem.atualizar(
                cliente,
                veiculo,
                motorista,
                request.tipoServico(),
                status,
                request.origem(),
                request.destino(),
                request.kmEstimado(),
                request.kmReal(),
                request.valorCobrado(),
                request.custoEstimado(),
                request.observacao(),

                request.veiculoClientePlaca(),
                request.veiculoClienteMarca(),
                request.veiculoClienteModelo(),
                request.veiculoClienteCor(),
                request.veiculoClienteAno(),
                request.veiculoClienteKm(),
                request.veiculoClienteObservacao()
        );

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse iniciarAtendimento(Long id) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Ordem de serviço cancelada não pode ser iniciada");
        }

        if (ordem.getStatus() == StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Ordem de serviço concluída não pode ser iniciada");
        }

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço faturada não pode ser iniciada");
        }

        if (ordem.getStatus() == StatusOrdemServico.EM_ATENDIMENTO) {
            throw new RegraNegocioException("Ordem de serviço já está em atendimento");
        }

        ordem.iniciarAtendimento();

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse concluir(Long id, BigDecimal kmReal) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Ordem de serviço cancelada não pode ser concluída");
        }

        if (ordem.getStatus() == StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Ordem de serviço já está concluída");
        }

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço faturada não pode ser concluída novamente");
        }

        if (ordem.getStatus() != StatusOrdemServico.EM_ATENDIMENTO) {
            throw new RegraNegocioException("Somente ordem em atendimento pode ser concluída");
        }

        ordem.concluir(kmReal);

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse faturar(Long id) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Ordem de serviço cancelada não pode ser faturada");
        }

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço já está faturada");
        }

        if (ordem.getStatus() != StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Somente ordem de serviço concluída pode ser faturada");
        }

        ordem.faturar();

        lancamentoFinanceiroService.registrarReceitaDaOrdemServico(ordem);

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public OrdemServicoResponse cancelar(Long id) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço faturada não pode ser cancelada");
        }

        if (ordem.getStatus() == StatusOrdemServico.CONCLUIDA) {
            throw new RegraNegocioException("Ordem de serviço concluída não pode ser cancelada");
        }

        if (ordem.getStatus() == StatusOrdemServico.CANCELADA) {
            throw new RegraNegocioException("Ordem de serviço já está cancelada");
        }

        ordem.cancelar();

        return new OrdemServicoResponse(ordem);
    }

    @Transactional
    public void deletar(Long id) {
        OrdemServico ordem = buscarEntidadePorId(id);

        if (ordem.getStatus() == StatusOrdemServico.FATURADA) {
            throw new RegraNegocioException("Ordem de serviço faturada não pode ser excluída");
        }

        ordemServicoRepository.delete(ordem);
    }

    private OrdemServico buscarEntidadePorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço não encontrada"));
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    private Veiculo buscarVeiculoOpcional(Long veiculoId) {
        if (veiculoId == null) {
            return null;
        }

        return veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }

    private Motorista buscarMotoristaOpcional(Long motoristaId) {
        if (motoristaId == null) {
            return null;
        }

        return motoristaRepository.findById(motoristaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));
    }
}
