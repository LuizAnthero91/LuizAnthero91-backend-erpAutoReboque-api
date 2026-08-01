package br.com.lcdigitaltec.autoreboque_tora.manutencao;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @GetMapping
    public List<ManutencaoResponse> listarTodos() {
        return manutencaoService.listarTodos();
    }

    @GetMapping("/{id}")
    public ManutencaoResponse buscarPorId(@PathVariable Long id) {
        return manutencaoService.buscarPorId(id);
    }

    @GetMapping("/veiculo/{veiculoId}")
    public List<ManutencaoResponse> listarPorVeiculo(@PathVariable Long veiculoId) {
        return manutencaoService.listarPorVeiculo(veiculoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManutencaoResponse cadastrar(@RequestBody @Valid ManutencaoRequest request) {
        return manutencaoService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public ManutencaoResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ManutencaoRequest request
    ) {
        return manutencaoService.atualizar(id, request);
    }

    @PatchMapping("/{id}/iniciar")
    public ManutencaoResponse iniciar(@PathVariable Long id) {
        return manutencaoService.iniciar(id);
    }

    @PatchMapping("/{id}/concluir")
    public ManutencaoResponse concluir(@PathVariable Long id) {
        return manutencaoService.concluir(id);
    }

    @PatchMapping("/{id}/cancelar")
    public ManutencaoResponse cancelar(@PathVariable Long id) {
        return manutencaoService.cancelar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        manutencaoService.deletar(id);
    }
}
