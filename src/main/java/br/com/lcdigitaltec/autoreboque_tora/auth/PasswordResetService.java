package br.com.lcdigitaltec.autoreboque_tora.auth;

import br.com.lcdigitaltec.autoreboque_tora.auth.dto.EsqueciSenhaResponse;
import br.com.lcdigitaltec.autoreboque_tora.domain.passwordreset.PasswordResetToken;
import br.com.lcdigitaltec.autoreboque_tora.domain.passwordreset.PasswordResetTokenRepository;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.Usuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.UsuarioRepository;
import br.com.lcdigitaltec.autoreboque_tora.security.ResetTokenService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class PasswordResetService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final ResetTokenService resetTokenService;
    private final PasswordEncoder passwordEncoder;

    private final boolean exposeToken;

    public PasswordResetService(
            UsuarioRepository usuarioRepository,
            PasswordResetTokenRepository tokenRepository,
            ResetTokenService resetTokenService,
            PasswordEncoder passwordEncoder,
            @Value("${app.password-reset.expose-token:false}")
            boolean exposeToken
    ) {

        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.resetTokenService = resetTokenService;
        this.passwordEncoder = passwordEncoder;
        this.exposeToken = exposeToken;

    }

    @Transactional
    public EsqueciSenhaResponse solicitarRecuperacao(
            String email
    ) {

        String mensagem =
                "Se o email estiver cadastrado, "
                + "as instruções de recuperação serão enviadas.";

        var usuarioOptional =
                usuarioRepository.findByEmail(email);

        if (usuarioOptional.isEmpty()) {

            return new EsqueciSenhaResponse(
                    mensagem,
                    null
            );
        }

        Usuario usuario =
                usuarioOptional.get();

        // Invalida solicitações anteriores
        tokenRepository.deleteByUsuarioId(
                usuario.getId()
        );

        String token =
                resetTokenService.gerarToken();


        String tokenHash =
                resetTokenService.gerarHash(token);

        Instant agora =
                Instant.now();

        PasswordResetToken reset =
                new PasswordResetToken();

        reset.setUsuario(usuario);

        reset.setTokenHash(tokenHash);

        reset.setCriadoEm(agora);

        reset.setExpiraEm(
                agora.plus(
                        30,
                        ChronoUnit.MINUTES
                )
        );

        tokenRepository.save(reset);

        return new EsqueciSenhaResponse(
                mensagem,
                exposeToken ? token : null
        );
    }


    @Transactional
    public void redefinirSenha(
            String token,
            String novaSenha,
            String confirmarSenha
    ) {

        if (!novaSenha.equals(confirmarSenha)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "As senhas não coincidem."
            );
        }

        String tokenHash =
                resetTokenService.gerarHash(token);

        PasswordResetToken reset =
                tokenRepository
                        .findByTokenHashAndUtilizadoEmIsNull(
                                tokenHash
                        )
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Token inválido ou já utilizado."
                                )
                        );

        Instant agora =
                Instant.now();

        if (reset.getExpiraEm()
                .isBefore(agora)) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Token expirado."
            );
        }

        Usuario usuario =
                reset.getUsuario();

        usuario.alterarSenha(
                passwordEncoder.encode(
                        novaSenha
                )
        );

        reset.setUtilizadoEm(agora);

        usuarioRepository.save(usuario);

        tokenRepository.save(reset);
    }
}