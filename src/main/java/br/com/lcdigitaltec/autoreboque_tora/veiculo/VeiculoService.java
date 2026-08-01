package br.com.lcdigitaltec.autoreboque_tora.veiculo;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponse> listarTodos() {
        return veiculoRepository.findAll()
                .stream()
                .map(VeiculoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public VeiculoResponse buscarPorId(Long id) {
        Veiculo veiculo = buscarEntidadePorId(id);
        return new VeiculoResponse(veiculo);
    }

    @Transactional
    public VeiculoResponse cadastrar(VeiculoRequest request) {
        String placa = request.placa().toUpperCase();

        if (veiculoRepository.existsByPlaca(placa)) {
            throw new  RegraNegocioException("Já existe veículo cadastrado com essa placa");
        }

        Veiculo veiculo = new Veiculo(
                placa,
                request.marca(),
                request.modelo(),
                request.ano(),
                request.tipo(),
                request.kmAtual(),
                request.observacao()
        );

        Veiculo salvo = veiculoRepository.save(veiculo);

        return new VeiculoResponse(salvo);
    }

    @Transactional
    public VeiculoResponse atualizar(Long id, VeiculoRequest request) {
        Veiculo veiculo = buscarEntidadePorId(id);

        String placa = request.placa().toUpperCase();

        veiculoRepository.findByPlaca(placa)
                .filter(outroVeiculo -> !outroVeiculo.getId().equals(id))
                .ifPresent(outroVeiculo -> {
                    throw new RegraNegocioException("Já existe veículo cadastrado com essa placa");
                });

        StatusVeiculo status = request.status() == null
                ? veiculo.getStatus()
                : request.status();

        veiculo.atualizar(
                placa,
                request.marca(),
                request.modelo(),
                request.ano(),
                request.tipo(),
                status,
                request.kmAtual(),
                request.observacao()
        );

        return new VeiculoResponse(veiculo);
    }

    @Transactional
    public void deletar(Long id) {
        Veiculo veiculo = buscarEntidadePorId(id);
        veiculoRepository.delete(veiculo);
    }

    private Veiculo buscarEntidadePorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }
}