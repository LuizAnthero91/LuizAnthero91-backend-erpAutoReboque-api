package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @GetMapping
    public List<OrdemServicoResponse> listarTodos() {
        return ordemServicoService.listarTodos();
    }

    @GetMapping("/{id}")
    public OrdemServicoResponse buscarPorId(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoResponse cadastrar(@RequestBody @Valid OrdemServicoRequest request) {
        return ordemServicoService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public OrdemServicoResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid OrdemServicoRequest request
    ) {
        return ordemServicoService.atualizar(id, request);
    }

    @PatchMapping("/{id}/iniciar")
    public OrdemServicoResponse iniciarAtendimento(@PathVariable Long id) {
        return ordemServicoService.iniciarAtendimento(id);
    }

    @PatchMapping("/{id}/concluir")
    public OrdemServicoResponse concluir(
            @PathVariable Long id,
            @RequestBody @Valid ConcluirOrdemServicoRequest request
    ) {
        return ordemServicoService.concluir(id, request.kmReal());
    }

    @PatchMapping("/{id}/faturar")
    public OrdemServicoResponse faturar(@PathVariable Long id) {
        return ordemServicoService.faturar(id);
    }

    @PatchMapping("/{id}/cancelar")
    public OrdemServicoResponse cancelar(@PathVariable Long id) {
        return ordemServicoService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        ordemServicoService.deletar(id);
    }
}