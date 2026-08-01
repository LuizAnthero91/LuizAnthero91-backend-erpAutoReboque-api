package br.com.lcdigitaltec.autoreboque_tora.cliente;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = buscarEntidadePorId(id);
        return new ClienteResponse(cliente);
    }

    @Transactional
    public ClienteResponse cadastrar(ClienteRequest request) {
        String documentoNormalizado = normalizarDocumento(request.documento());

        validarDocumentoDuplicado(documentoNormalizado, null);

        Cliente cliente = new Cliente(
                request.nome(),
                documentoNormalizado,
                request.telefone(),
                request.email(),
                request.tipo(),
                request.endereco(),
                request.observacao()
        );

        Cliente salvo = clienteRepository.save(cliente);

        return new ClienteResponse(salvo);
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);

        String documentoNormalizado = normalizarDocumento(request.documento());

        validarDocumentoDuplicado(documentoNormalizado, id);

        StatusCliente status = request.status() == null
                ? cliente.getStatus()
                : request.status();

        cliente.atualizar(
                request.nome(),
                documentoNormalizado,
                request.telefone(),
                request.email(),
                request.tipo(),
                status,
                request.endereco(),
                request.observacao()
        );

        return new ClienteResponse(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        Cliente cliente = buscarEntidadePorId(id);
        clienteRepository.delete(cliente);
    }

    private Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));
    }

    private void validarDocumentoDuplicado(String documento, Long idAtual) {
        if (documento == null) {
            return;
        }

        clienteRepository.findByDocumento(documento)
                .filter(cliente -> idAtual == null || !cliente.getId().equals(idAtual))
                .ifPresent(cliente -> {
                    throw new RegraNegocioException("Já existe cliente cadastrado com esse documento");
                });
    }

    private String normalizarDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            return null;
        }

        return documento.replaceAll("\\D", "");
    }
}
