package br.com.lcdigitaltec.autoreboque_tora.motorista;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotoristaService {

    private final MotoristaRepository motoristaRepository;

    public MotoristaService(MotoristaRepository motoristaRepository) {
        this.motoristaRepository = motoristaRepository;
    }

    @Transactional(readOnly = true)
    public List<MotoristaResponse> listarTodos() {
        return motoristaRepository.findAll()
                .stream()
                .map(MotoristaResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MotoristaResponse buscarPorId(Long id) {
        Motorista motorista = buscarEntidadePorId(id);
        return new MotoristaResponse(motorista);
    }

    @Transactional
    public MotoristaResponse cadastrar(MotoristaRequest request) {
        String cpfNormalizado = normalizarDocumento(request.cpf());
        String cnhNormalizada = normalizarTexto(request.cnh());

        validarCpfDuplicado(cpfNormalizado, null);
        validarCnhDuplicada(cnhNormalizada, null);

        Motorista motorista = new Motorista(
                request.nome(),
                cpfNormalizado,
                request.telefone(),
                cnhNormalizada,
                request.categoriaCnh(),
                request.validadeCnh(),
                request.observacao()
        );

        Motorista salvo = motoristaRepository.save(motorista);

        return new MotoristaResponse(salvo);
    }

    @Transactional
    public MotoristaResponse atualizar(Long id, MotoristaRequest request) {
        Motorista motorista = buscarEntidadePorId(id);

        String cpfNormalizado = normalizarDocumento(request.cpf());
        String cnhNormalizada = normalizarTexto(request.cnh());

        validarCpfDuplicado(cpfNormalizado, id);
        validarCnhDuplicada(cnhNormalizada, id);

        StatusMotorista status = request.status() == null
                ? motorista.getStatus()
                : request.status();

        motorista.atualizar(
                request.nome(),
                cpfNormalizado,
                request.telefone(),
                cnhNormalizada,
                request.categoriaCnh(),
                request.validadeCnh(),
                status,
                request.observacao()
        );

        return new MotoristaResponse(motorista);
    }

    @Transactional
    public void deletar(Long id) {
        Motorista motorista = buscarEntidadePorId(id);
        motoristaRepository.delete(motorista);
    }

    private Motorista buscarEntidadePorId(Long id) {
        return motoristaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado"));
    }

    private void validarCpfDuplicado(String cpf, Long idAtual) {
        if (cpf == null) {
            return;
        }

        motoristaRepository.findByCpf(cpf)
                .filter(motorista -> idAtual == null || !motorista.getId().equals(idAtual))
                .ifPresent(motorista -> {
                    throw new RegraNegocioException("Já existe motorista cadastrado com esse CPF");
                });
    }

    private void validarCnhDuplicada(String cnh, Long idAtual) {
        if (cnh == null) {
            return;
        }

        motoristaRepository.findByCnh(cnh)
                .filter(motorista -> idAtual == null || !motorista.getId().equals(idAtual))
                .ifPresent(motorista -> {
                    throw new RegraNegocioException("Já existe motorista cadastrado com essa CNH");
                });
    }

    private String normalizarDocumento(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.replaceAll("\\D", "");
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }

        return valor.trim().toUpperCase();
    }
}
