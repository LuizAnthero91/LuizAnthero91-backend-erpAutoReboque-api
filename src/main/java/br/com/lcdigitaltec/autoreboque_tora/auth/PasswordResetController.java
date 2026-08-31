package br.com.lcdigitaltec.autoreboque_tora.auth;

import br.com.lcdigitaltec.autoreboque_tora.auth.dto.EsqueciSenhaRequest;
import br.com.lcdigitaltec.autoreboque_tora.auth.dto.EsqueciSenhaResponse;
import br.com.lcdigitaltec.autoreboque_tora.auth.dto.RedefinirSenhaRequest;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(
            PasswordResetService passwordResetService
    ) {
        this.passwordResetService = passwordResetService;
    }


    @PostMapping("/esqueci-senha")
    public ResponseEntity<EsqueciSenhaResponse> esqueciSenha(
            @Valid
            @RequestBody EsqueciSenhaRequest request
    ) {

        EsqueciSenhaResponse response =
                passwordResetService
                        .solicitarRecuperacao(
                                request.email()
                        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(
            @Valid
            @RequestBody RedefinirSenhaRequest request
    ) {

        passwordResetService.redefinirSenha(
                request.token(),
                request.novaSenha(),
                request.confirmarSenha()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}