package br.com.lcdigitaltec.autoreboque_tora.auth;

import br.com.lcdigitaltec.autoreboque_tora.security.JwtService;
import br.com.lcdigitaltec.autoreboque_tora.usuario.Usuario;
import br.com.lcdigitaltec.autoreboque_tora.usuario.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
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

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        User userDetails = new User(
                usuario.getEmail(),
                usuario.getSenhaHash(),
                usuario.getAtivo(),
                true,
                true,
                true,
                java.util.List.of(() -> "ROLE_" + usuario.getPerfil().name())
        );

        String token = jwtService.gerarToken(
                userDetails,
                Map.of(
                        "usuarioId", usuario.getId(),
                        "nome", usuario.getNome(),
                        "perfil", usuario.getPerfil().name()
                )
        );

        return new LoginResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    public UsuarioLogadoResponse buscarUsuarioLogado(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UsuarioLogadoResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }
}