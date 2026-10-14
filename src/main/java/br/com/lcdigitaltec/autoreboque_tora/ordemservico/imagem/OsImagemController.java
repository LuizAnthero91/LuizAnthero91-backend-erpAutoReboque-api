package br.com.lcdigitaltec.autoreboque_tora.ordemservico.imagem;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(
        "/api/ordens-servico/{ordemServicoId}/imagens"
)
public class OsImagemController {

    private final OsImagemService service;

    public OsImagemController(
            OsImagemService service
    ) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OsImagemResponse> upload(
            @PathVariable Long ordemServicoId,
            @RequestParam("file") MultipartFile file
    ) {

        return ResponseEntity.ok(
                service.upload(
                        ordemServicoId,
                        file
                )
        );
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OsImagemResponse>> listar(
            @PathVariable Long ordemServicoId
    ) {

        return ResponseEntity.ok(
                service.listar(
                        ordemServicoId
                )
        );
    }

    @DeleteMapping("/{imagemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> excluir(
            @PathVariable Long ordemServicoId,
            @PathVariable Long imagemId
    ) {

        service.excluir(
                ordemServicoId,
                imagemId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}