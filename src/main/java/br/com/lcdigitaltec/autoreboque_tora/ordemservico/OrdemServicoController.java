package br.com.lcdigitaltec.autoreboque_tora.ordemservico;

import br.com.lcdigitaltec.autoreboque_tora.shared.pagination.PaginaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;



@RestController
@RequestMapping("/api/ordens-servico")
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    public OrdemServicoController(OrdemServicoService ordemServicoService) {
        this.ordemServicoService = ordemServicoService;
    }

    @PreAuthorize("hasAuthority('OS_VISUALIZAR')")
    @GetMapping
    public PaginaResponse<OrdemServicoResponse> listarTodos(
            @PageableDefault(
                    size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable
    ) {
        return ordemServicoService.listarTodos(pageable);
    }



    @PreAuthorize("hasAuthority('OS_VISUALIZAR')")
    @GetMapping("/{id}")
    public OrdemServicoResponse buscarPorId(@PathVariable Long id) {
        return ordemServicoService.buscarPorId(id);
    }

    @PreAuthorize("hasAuthority('OS_VISUALIZAR')")
    @GetMapping("/numero/{numeroOs}")
    public OrdemServicoResponse buscarPorNumero(
            @PathVariable Long numeroOs
    ) {
        return ordemServicoService.buscarPorNumero(numeroOs);
    }

    @PreAuthorize("hasAuthority('OS_CRIAR')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdemServicoResponse cadastrar(@RequestBody @Valid OrdemServicoRequest request) {
        return ordemServicoService.cadastrar(request);
    }

    @PreAuthorize("hasAuthority('OS_EDITAR')")
    @PutMapping("/{id}")
    public OrdemServicoResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid OrdemServicoRequest request
    ) {
        return ordemServicoService.atualizar(id, request);
    }

    @PreAuthorize("hasAuthority('OS_EDITAR')")
    @PatchMapping("/{id}/iniciar")
    public OrdemServicoResponse iniciarAtendimento(@PathVariable Long id) {
        return ordemServicoService.iniciarAtendimento(id);
    }

    @PreAuthorize("hasAuthority('OS_EDITAR')")
    @PatchMapping("/{id}/concluir")
    public OrdemServicoResponse concluir(
            @PathVariable Long id,
            @RequestBody @Valid ConcluirOrdemServicoRequest request
    ) {
        return ordemServicoService.concluir(id, request.kmReal());
    }

    @PreAuthorize("hasAuthority('FINANCEIRO_EDITAR')")
    @PatchMapping("/{id}/faturar")
    public OrdemServicoResponse faturar(@PathVariable Long id) {
        return ordemServicoService.faturar(id);
    }

    @PreAuthorize("hasAuthority('OS_CANCELAR')")
    @PatchMapping("/{id}/cancelar")
    public OrdemServicoResponse cancelar(@PathVariable Long id) {
        return ordemServicoService.cancelar(id);
    }

    @PreAuthorize("authentication.principal.perfil.nome == 'ADMIN'")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        ordemServicoService.deletar(id);
    }
}
