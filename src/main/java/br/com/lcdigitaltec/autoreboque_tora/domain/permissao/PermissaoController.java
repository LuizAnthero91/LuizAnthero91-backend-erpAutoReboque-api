package br.com.lcdigitaltec.autoreboque_tora.domain.permissao;

import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.dto.DadosDetalhamentoPermissao;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissoes")
public class PermissaoController {

    private final PermissaoService permissaoService;

    public PermissaoController(
            PermissaoService permissaoService
    ) {
        this.permissaoService =
                permissaoService;
    }

    @PreAuthorize("hasAuthority('PERFIL_VISUALIZAR')")
    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoPermissao>> listar() {

        return ResponseEntity.ok(
                permissaoService.listar()
        );
    }

    @PreAuthorize("hasAuthority('PERFIL_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPermissao> detalhar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                permissaoService.detalhar(id)
        );
    }
}