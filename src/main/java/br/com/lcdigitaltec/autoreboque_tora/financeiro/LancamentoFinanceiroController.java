package br.com.lcdigitaltec.autoreboque_tora.financeiro;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/financeiro/lancamentos")
public class LancamentoFinanceiroController {

    private final LancamentoFinanceiroService lancamentoFinanceiroService;

    public LancamentoFinanceiroController(LancamentoFinanceiroService lancamentoFinanceiroService) {
        this.lancamentoFinanceiroService = lancamentoFinanceiroService;
    }

    @GetMapping
    public List<LancamentoFinanceiroResponse> listarTodos() {
        return lancamentoFinanceiroService.listarTodos();
    }

    @GetMapping("/{id}")
    public LancamentoFinanceiroResponse buscarPorId(@PathVariable Long id) {
        return lancamentoFinanceiroService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LancamentoFinanceiroResponse cadastrar(
            @RequestBody @Valid LancamentoFinanceiroRequest request
    ) {
        return lancamentoFinanceiroService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public LancamentoFinanceiroResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid LancamentoFinanceiroRequest request
    ) {
        return lancamentoFinanceiroService.atualizar(id, request);
    }

    @PatchMapping("/{id}/pagar")
    public LancamentoFinanceiroResponse marcarComoPago(@PathVariable Long id) {
        return lancamentoFinanceiroService.marcarComoPago(id);
    }

    @PatchMapping("/{id}/cancelar")
    public LancamentoFinanceiroResponse cancelar(@PathVariable Long id) {
        return lancamentoFinanceiroService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        lancamentoFinanceiroService.deletar(id);
    }

    @GetMapping("/resumo")
    public ResumoFinanceiroResponse resumo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim
    ) {
        return lancamentoFinanceiroService.resumo(inicio, fim);
    }
}