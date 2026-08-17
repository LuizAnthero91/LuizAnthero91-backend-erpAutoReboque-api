package br.com.lcdigitaltec.autoreboque_tora.domain.usuario;

import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosAtualizacaoUsuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosCadastroUsuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosDetalhamentoUsuario;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService
    ) {
        this.usuarioService = usuarioService;
    }


    @PreAuthorize("hasAuthority('USUARIO_CRIAR')")
    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuario>
    cadastrar(
            @RequestBody
            @Valid
            DadosCadastroUsuario dados
    ) {

        DadosDetalhamentoUsuario usuario =
                usuarioService.cadastrar(dados);

        URI uri =
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(
                                usuario.id()
                        )
                        .toUri();

        return ResponseEntity
                .created(uri)
                .body(usuario);
    }


    @PreAuthorize("hasAuthority('USUARIO_VISUALIZAR')")
    @GetMapping
    public ResponseEntity<
            List<DadosDetalhamentoUsuario>
            > listar() {

        return ResponseEntity.ok(
                usuarioService.listar()
        );
    }

    @PreAuthorize("hasAuthority('USUARIO_VISUALIZAR')")
    @GetMapping("/{id}")
    public ResponseEntity<
            DadosDetalhamentoUsuario
            > detalhar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                usuarioService.detalhar(id)
        );
    }


    @PreAuthorize("hasAuthority('USUARIO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<
            DadosDetalhamentoUsuario
            > atualizar(

            @PathVariable Long id,

            @RequestBody
            @Valid
            DadosAtualizacaoUsuario dados
    ) {

        return ResponseEntity.ok(
                usuarioService.atualizar(
                        id,
                        dados
                )
        );
    }

    @PreAuthorize("hasAuthority('USUARIO_DESATIVAR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(
            @PathVariable Long id
    ) {

        usuarioService.desativar(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PreAuthorize("hasAuthority('USUARIO_EDITAR')")
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<
            DadosDetalhamentoUsuario
            > ativar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                usuarioService.ativar(id)
        );
    }
}