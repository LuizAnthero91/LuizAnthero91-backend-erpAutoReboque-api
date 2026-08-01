package br.com.lcdigitaltec.autoreboque_tora.motorista;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/motoristas")
public class MotoristaController {

    private final MotoristaService motoristaService;

    public MotoristaController(MotoristaService motoristaService) {
        this.motoristaService = motoristaService;
    }

    @GetMapping
    public List<MotoristaResponse> listarTodos() {
        return motoristaService.listarTodos();
    }

    @GetMapping("/{id}")
    public MotoristaResponse buscarPorId(@PathVariable Long id) {
        return motoristaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MotoristaResponse cadastrar(@RequestBody @Valid MotoristaRequest request) {
        return motoristaService.cadastrar(request);
    }

    @PutMapping("/{id}")
    public MotoristaResponse atualizar(
            @PathVariable Long id,
            @RequestBody @Valid MotoristaRequest request
    ) {
        return motoristaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        motoristaService.deletar(id);
    }
}
