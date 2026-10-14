package br.com.lcdigitaltec.autoreboque_tora.controller;

import br.com.lcdigitaltec.autoreboque_tora.controller.dto.OsImagemResponse;
import br.com.lcdigitaltec.autoreboque_tora.domain.ordemservico.imagem.OsImagem;
import br.com.lcdigitaltec.autoreboque_tora.domain.ordemservico.imagem.OsImagemService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
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
    public ResponseEntity<OsImagemResponse> upload(
            @PathVariable Long ordemServicoId,

            @RequestParam("file")
            MultipartFile file
    ) {

        OsImagem imagem =
                service.upload(
                        ordemServicoId,
                        file
                );

        OsImagemResponse response =
                OsImagemResponse.from(
                        imagem
                );

        return ResponseEntity
                .created(
                        URI.create(
                                response.url()
                        )
                )
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<OsImagemResponse>>
    listar(
            @PathVariable Long ordemServicoId
    ) {

        List<OsImagemResponse> imagens =
                service.listar(
                                ordemServicoId
                        )
                        .stream()
                        .map(
                                OsImagemResponse::from
                        )
                        .toList();

        return ResponseEntity.ok(
                imagens
        );
    }

    @DeleteMapping("/{imagemId}")
    public ResponseEntity<Void> excluir(
            @PathVariable Long ordemServicoId,
            @PathVariable Long imagemId
    ) {

        service.excluir(
                imagemId
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}