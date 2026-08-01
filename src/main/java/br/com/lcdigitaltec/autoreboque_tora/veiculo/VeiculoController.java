package br.com.lcdigitaltec.autoreboque_tora.veiculo;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping
    public List<VeiculoResponse> listarTodos() {
        return veiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    public VeiculoResponse buscarPorId(@PathVariable Long id) {
        return veiculoService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeiculoResponse cadastrar(@RequestBody @Valid VeiculoRequest request) {
        return veiculoService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public VeiculoResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid VeiculoRequest request
    ) {
        return veiculoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        veiculoService.deletar(id);
    }
}