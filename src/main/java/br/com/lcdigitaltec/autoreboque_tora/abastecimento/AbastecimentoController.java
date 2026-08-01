package br.com.lcdigitaltec.autoreboque_tora.abastecimento;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abastecimentos")
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    public AbastecimentoController(AbastecimentoService abastecimentoService) {
        this.abastecimentoService = abastecimentoService;
    }

    @GetMapping
    public List<AbastecimentoResponse> listarTodos() {
        return abastecimentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public AbastecimentoResponse buscarPorId(@PathVariable Long id) {
        return abastecimentoService.buscarPorId(id);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<AbastecimentoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return abastecimentoService.listarPorVeiculo(veiculoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AbastecimentoResponse cadastrar(@RequestBody @Valid AbastecimentoRequest request) {
        return abastecimentoService.cadastrar(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        abastecimentoService.deletar(id);
    }
}