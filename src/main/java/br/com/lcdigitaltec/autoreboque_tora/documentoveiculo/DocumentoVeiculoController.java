package br.com.lcdigitaltec.autoreboque_tora.documentoveiculo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documentos-veiculos")
public class DocumentoVeiculoController {

    private final DocumentoVeiculoService documentoVeiculoService;

    public DocumentoVeiculoController(DocumentoVeiculoService documentoVeiculoService) {
        this.documentoVeiculoService = documentoVeiculoService;
    }

    @GetMapping
    public List<DocumentoVeiculoResponse> listarTodos() {
        return documentoVeiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public DocumentoVeiculoResponse buscarPorId(@PathVariable Long id) {
        return documentoVeiculoService.buscarPorId(id);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<DocumentoVeiculoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return documentoVeiculoService.listarPorVeiculo(veiculoId);
    }

    @GetMapping("/status/{status}")
    public List<DocumentoVeiculoResponse> listarPorStatus(@PathVariable StatusDocumentoVeiculo status) {
        return documentoVeiculoService.listarPorStatus(status);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentoVeiculoResponse cadastrar(@RequestBody @Valid DocumentoVeiculoRequest request) {
        return documentoVeiculoService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public DocumentoVeiculoResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DocumentoVeiculoRequest request
    ) {
        return documentoVeiculoService.atualizar(id, request);
    }

    @PatchMapping("/{id}/atualizar-status")
    public DocumentoVeiculoResponse atualizarStatus(@PathVariable Long id) {
        return documentoVeiculoService.atualizarStatus(id);
    }

    @PatchMapping("/{id}/gerar-despesa")
    public DocumentoVeiculoResponse gerarDespesa(@PathVariable Long id) {
        return documentoVeiculoService.gerarDespesa(id);
    }

    @PatchMapping("/{id}/cancelar")
    public DocumentoVeiculoResponse cancelar(@PathVariable Long id) {
        return documentoVeiculoService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        documentoVeiculoService.deletar(id);
    }
}