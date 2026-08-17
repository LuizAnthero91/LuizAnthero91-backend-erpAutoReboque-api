package br.com.lcdigitaltec.autoreboque_tora.domain.perfil;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosAtualizacaoPerfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosCadastroPerfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosDetalhamentoPerfil;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PreAuthorize("hasAuthority('PERFIL_GERENCIAR')")
    @PostMapping
    public ResponseEntity<DadosDetalhamentoPerfil> cadastrar(
            @RequestBody @Valid DadosCadastroPerfil dados
    ) {

        DadosDetalhamentoPerfil perfil =
                perfilService.cadastrar(dados);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(perfil.id())
                .toUri();

        return ResponseEntity
                .created(uri)
                .body(perfil);
    }

    @PreAuthorize("hasAuthority('PERFIL_VISUALIZAR')")
    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoPerfil>> listar() {

        return ResponseEntity.ok(
                perfilService.listar()
        );
    }

    @PreAuthorize("hasAuthority('PERFIL_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerfil> detalhar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                perfilService.detalhar(id)
        );
    }

    @PreAuthorize("hasAuthority('PERFIL_GERENCIAR')")
    @PutMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPerfil> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid DadosAtualizacaoPerfil dados
    ) {

        return ResponseEntity.ok(
                perfilService.atualizar(id, dados)
        );
    }

    @PreAuthorize("hasAuthority('PERFIL_GERENCIAR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable Long id
    ) {

        perfilService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasAuthority('PERFIL_GERENCIAR')")
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<DadosDetalhamentoPerfil> ativar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                perfilService.ativar(id)
        );
    }
}