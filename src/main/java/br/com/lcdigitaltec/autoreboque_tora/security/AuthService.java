package br.com.lcdigitaltec.autoreboque_tora.security;

import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.Usuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.UsuarioRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );

        Usuario usuario = usuarioRepository
                .findByEmail(request.email())
                .orElseThrow(
                        () -> new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        String token = jwtService.gerarToken(
                usuario,
                Map.of(
                        "usuarioId", usuario.getId(),
                        "nome", usuario.getNome(),
                        "perfil", usuario.getPerfil().getNome()
                )
        );

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().getNome()
        );
    }

    public UsuarioLogadoResponse buscarUsuarioLogado(
            String email
    ) {

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        return new UsuarioLogadoResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().getNome(),
                usuario.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .sorted()
                        .toList()
        );
    }
}