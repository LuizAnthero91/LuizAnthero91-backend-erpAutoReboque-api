package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import br.com.lcdigitaltec.autoreboque_tora.common.exception.RecursoNaoEncontradoException;
import br.com.lcdigitaltec.autoreboque_tora.common.exception.RegraNegocioException;
import br.com.lcdigitaltec.autoreboque_tora.financeiro.LancamentoFinanceiroService;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.Veiculo;
import br.com.lcdigitaltec.autoreboque_tora.veiculo.VeiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentoVeiculoService {

    private final DocumentoVeiculoRepository documentoVeiculoRepository;
    private final VeiculoRepository veiculoRepository;
    private final LancamentoFinanceiroService lancamentoFinanceiroService;

    public DocumentoVeiculoService(
            DocumentoVeiculoRepository documentoVeiculoRepository,
            VeiculoRepository veiculoRepository,
            LancamentoFinanceiroService lancamentoFinanceiroService
    ) {
        this.documentoVeiculoRepository = documentoVeiculoRepository;
        this.veiculoRepository = veiculoRepository;
        this.lancamentoFinanceiroService = lancamentoFinanceiroService;
    }

    @Transactional(readOnly = true)
    public List<DocumentoVeiculoResponse> listarTodos() {
        return documentoVeiculoRepository.findAllByOrderByDataVencimentoAsc()
                .stream()
                .map(DocumentoVeiculoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public DocumentoVeiculoResponse buscarPorId(Long id) {
        DocumentoVeiculo documento = buscarEntidadePorId(id);
        return new DocumentoVeiculoResponse(documento);
    }

    @Transactional(readOnly = true)
    public List<DocumentoVeiculoResponse> listarPorVeiculo(Long veiculoId) {
        buscarVeiculo(veiculoId);

        return documentoVeiculoRepository.findByVeiculoIdOrderByDataVencimentoAsc(veiculoId)
                .stream()
                .map(DocumentoVeiculoResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentoVeiculoResponse> listarPorStatus(StatusDocumentoVeiculo status) {
        return documentoVeiculoRepository.findByStatusOrderByDataVencimentoAsc(status)
                .stream()
                .map(DocumentoVeiculoResponse::new)
                .toList();
    }

    @Transactional
    public DocumentoVeiculoResponse cadastrar(DocumentoVeiculoRequest request) {
        validarDatas(request.dataEmissao(), request.dataVencimento());

        Veiculo veiculo = buscarVeiculo(request.veiculoId());

        DocumentoVeiculo documento = new DocumentoVeiculo(
                veiculo,
                request.tipo(),
                request.status(),
                request.numeroDocumento(),
                request.dataEmissao(),
                request.dataVencimento(),
                request.valor(),
                request.orgaoEmissor(),
                request.arquivoUrl(),
                request.observacao()
        );

        DocumentoVeiculo salvo = documentoVeiculoRepository.save(documento);

        return new DocumentoVeiculoResponse(salvo);
    }

    @Transactional
    public DocumentoVeiculoResponse atualizar(Long id, DocumentoVeiculoRequest request) {
        validarDatas(request.dataEmissao(), request.dataVencimento());

        DocumentoVeiculo documento = buscarEntidadePorId(id);

        if (documento.isDespesaGerada()) {
            throw new RegraNegocioException("Documento com despesa gerada não pode ser alterado");
        }

        if (documento.getStatus() == StatusDocumentoVeiculo.CANCELADO) {
            throw new RegraNegocioException("Documento cancelado não pode ser alterado");
        }

        documento.atualizar(
                request.tipo(),
                request.status(),
                request.numeroDocumento(),
                request.dataEmissao(),
                request.dataVencimento(),
                request.valor(),
                request.orgaoEmissor(),
                request.arquivoUrl(),
                request.observacao()
        );

        return new DocumentoVeiculoResponse(documento);
    }

    @Transactional
    public DocumentoVeiculoResponse atualizarStatus(Long id) {
        DocumentoVeiculo documento = buscarEntidadePorId(id);

        if (documento.getStatus() == StatusDocumentoVeiculo.CANCELADO) {
            throw new RegraNegocioException("Documento cancelado não pode ter status atualizado por vencimento");
        }

        documento.atualizarStatusPorVencimento();

        return new DocumentoVeiculoResponse(documento);
    }

    @Transactional
    public DocumentoVeiculoResponse gerarDespesa(Long id) {
        DocumentoVeiculo documento = buscarEntidadePorId(id);

        if (documento.getStatus() == StatusDocumentoVeiculo.CANCELADO) {
            throw new RegraNegocioException("Documento cancelado não pode gerar despesa");
        }

        if (documento.isDespesaGerada()) {
            throw new RegraNegocioException("Despesa desse documento já foi gerada");
        }

        if (documento.getValor() == null || documento.getValor().signum() <= 0) {
            throw new RegraNegocioException("Documento sem valor para gerar despesa");
        }

        lancamentoFinanceiroService.registrarDespesaDocumentacao(
                documento.getVeiculo(),
                documento.getValor(),
                LocalDate.now(),
                "Despesa gerada pelo documento " + documento.getTipo() + " #" + documento.getId()
        );

        documento.marcarDespesaGerada();

        return new DocumentoVeiculoResponse(documento);
    }

    @Transactional
    public DocumentoVeiculoResponse cancelar(Long id) {
        DocumentoVeiculo documento = buscarEntidadePorId(id);

        if (documento.getStatus() == StatusDocumentoVeiculo.CANCELADO) {
            throw new RegraNegocioException("Documento já está cancelado");
        }

        if (documento.isDespesaGerada()) {
            throw new RegraNegocioException("Documento com despesa gerada não pode ser cancelado");
        }

        documento.cancelar();

        return new DocumentoVeiculoResponse(documento);
    }

    @Transactional
    public void deletar(Long id) {
        DocumentoVeiculo documento = buscarEntidadePorId(id);

        if (documento.isDespesaGerada()) {
            throw new RegraNegocioException("Documento com despesa gerada não pode ser excluído");
        }

        documentoVeiculoRepository.delete(documento);
    }

    private DocumentoVeiculo buscarEntidadePorId(Long id) {
        return documentoVeiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Documento do veículo não encontrado"));
    }

    private Veiculo buscarVeiculo(Long veiculoId) {
        return veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));
    }

    private void validarDatas(LocalDate dataEmissao, LocalDate dataVencimento) {
        if (dataVencimento == null) {
            throw new RegraNegocioException("Data de vencimento é obrigatória");
        }

        if (dataEmissao != null && dataVencimento.isBefore(dataEmissao)) {
            throw new RegraNegocioException("Data de vencimento não pode ser anterior à data de emissão");
        }
    }
}
