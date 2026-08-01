package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroService;
import br.com.lcdigitaltec.autoreboque_tora.motorista.Motorista;
import br.com.lcdigitaltec.autoreboque_tora.motorista.MotoristaRepository;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final VeiculoRepository veiculoRepository;
    private final MotoristaRepository motoristaRepository;
    private final LancamentoFinanceiroService lancamentoFinanceiroService;

    public AbastecimentoService(
            AbastecimentoRepository abastecimentoRepository,
            VeiculoRepository veiculoRepository,
            MotoristaRepository motoristaRepository,
            LancamentoFinanceiroService lancamentoFinanceiroService
    ) {
        this.abastecimentoRepository = abastecimentoRepository;
        this.veiculoRepository = veiculoRepository;
        this.motoristaRepository = motoristaRepository;
        this.lancamentoFinanceiroService = lancamentoFinanceiroService;
    }

    @Transactional(readOnly = true)
    public List<AbastecimentoResponse> listarTodos() {
        return abastecimentoRepository.findAll()
                .stream()
                .map(AbastecimentoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public AbastecimentoResponse buscarPorId(Long id) {
        Abastecimento abastecimento = buscarEntidadePorId(id);
        return new AbastecimentoResponse(abastecimento);
    }

    @Transactional(readOnly = true)
    public List<AbastecimentoResponse> listarPorVeiculo(Long veiculoId) {
        return abastecimentoRepository.findByVeiculoId(veiculoId)
                .stream()
                .map(AbastecimentoResponse::new)
                .toList();
    }

    @Transactional
    public AbastecimentoResponse cadastrar(AbastecimentoRequest request) {
        Veiculo veiculo = buscarVeiculo(request.veiculoId());
        Motorista motorista = buscarMotoristaOpcional(request.motoristaId());

        BigDecimal valorTotal = request.litros()
                .multiply(request.valorLitro())
                .setScale(2, RoundingMode.HALF_UP);

        Abastecimento abastecimento = new Abastecimento(
                veiculo,
                motorista,
                request.dataAbastecimento(),
                request.kmAtual(),
                request.litros(),
                request.valorLitro(),
                valorTotal,
                request.posto(),
                request.observacao()
        );

        Abastecimento salvo = abastecimentoRepository.save(abastecimento);

        veiculo.atualizarKmAtual(request.kmAtual());

        lancamentoFinanceiroService.registrarDespesaDiesel(
                veiculo,
                valorTotal,
                request.dataAbastecimento(),
                "Despesa gerada pelo abastecimento #" + salvo.getId()
        );

        return new AbastecimentoResponse(salvo);
    }

    @Transactional
    public void deletar(Long id) {
        Abastecimento abastecimento = buscarEntidadePorId(id);
        abastecimentoRepository.delete(abastecimento);
    }

    private Abastecimento buscarEntidadePorId(Long id) {
        return abastecimentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Abastecimento não encontrado"));
    }

    private Veiculo buscarVeiculo(Long veiculoId) {
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
