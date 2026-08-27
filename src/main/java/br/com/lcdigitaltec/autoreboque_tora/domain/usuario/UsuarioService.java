package br.com.lcdigitaltec.autoreboque_tora.domain.usuario;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.Perfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.PerfilRepository;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosAtualizacaoUsuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosCadastroUsuario;
import br.com.lcdigitaltec.autoreboque_tora.domain.usuario.dto.DadosDetalhamentoUsuario;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
        this.passwordEncoder = passwordEncoder;
    }



    @Transactional
    public DadosDetalhamentoUsuario cadastrar(
            DadosCadastroUsuario dados
    ) {

        validarEmailDuplicado(
                dados.email()
        );

        Perfil perfil = buscarPerfilAtivo(
                dados.perfilId()
        );

        String senhaHash =
                passwordEncoder.encode(
                        dados.senha()
                );

        Usuario usuario = new Usuario(
                dados.nome(),
                dados.email(),
                senhaHash,
                perfil
        );

        usuarioRepository.save(usuario);

        return new DadosDetalhamentoUsuario(
                usuario
        );
    }


    @Transactional(readOnly = true)
    public List<DadosDetalhamentoUsuario> listar() {

        return usuarioRepository
                .findAll()
                .stream()
                .map(DadosDetalhamentoUsuario::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public DadosDetalhamentoUsuario detalhar(
            Long id
    ) {

        Usuario usuario = buscarUsuario(id);

        return new DadosDetalhamentoUsuario(
                usuario
        );
    }



    @Transactional
    public DadosDetalhamentoUsuario atualizar(
            Long id,
            DadosAtualizacaoUsuario dados
    ) {

        Usuario usuario = buscarUsuario(id);

        atualizarNome(
                usuario,
                dados
        );

        atualizarEmail(
                usuario,
                dados
        );

        atualizarSenha(
                usuario,
                dados
        );

        atualizarPerfil(
                usuario,
                dados
        );

        atualizarStatus(
                usuario,
                dados
        );

        return new DadosDetalhamentoUsuario(
                usuario
        );
    }



    @Transactional
    public void desativar(
            Long id
    ) {

        Usuario usuario = buscarUsuario(id);

        usuario.desativar();
    }



    @Transactional
    public DadosDetalhamentoUsuario ativar(
            Long id
    ) {

        Usuario usuario = buscarUsuario(id);

        if (!usuario.getPerfil().isAtivo()) {

            throw new IllegalArgumentException(
                    "Não é possível ativar o usuário porque o perfil está inativo"
            );
        }

        usuario.ativar();

        return new DadosDetalhamentoUsuario(
                usuario
        );
    }



    private Usuario buscarUsuario(
            Long id
    ) {

        return usuarioRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Usuário não encontrado"
                        )
                );
    }



    private Perfil buscarPerfilAtivo(
            Long perfilId
    ) {

        Perfil perfil = perfilRepository
                .findById(perfilId)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Perfil não encontrado"
                        )
                );

        if (!perfil.isAtivo()) {

            throw new IllegalArgumentException(
                    "Não é possível utilizar um perfil inativo"
            );
        }

        return perfil;
    }



    private void validarEmailDuplicado(
            String email
    ) {

        if (usuarioRepository
                .existsByEmailIgnoreCase(email)) {

            throw new IllegalArgumentException(
                    "Já existe um usuário cadastrado com esse e-mail"
            );
        }
    }



    private void atualizarNome(
            Usuario usuario,
            DadosAtualizacaoUsuario dados
    ) {

        if (dados.nome() == null) {
            return;
        }

        usuario.alterarNome(
                dados.nome()
        );
    }



    private void atualizarEmail(
            Usuario usuario,
            DadosAtualizacaoUsuario dados
    ) {

        if (dados.email() == null) {
            return;
        }

        boolean emailEmUso =
                usuarioRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                dados.email(),
                                usuario.getId()
                        );

        if (emailEmUso) {

            throw new IllegalArgumentException(
                    "Já existe outro usuário cadastrado com esse e-mail"
            );
        }

        usuario.alterarEmail(
                dados.email()
        );
    }


    private void atualizarSenha(
            Usuario usuario,
            DadosAtualizacaoUsuario dados
    ) {

        if (dados.senha() == null) {
            return;
        }

        String novaSenhaHash =
                passwordEncoder.encode(
                        dados.senha()
                );

        usuario.alterarSenha(
                novaSenhaHash
        );
    }


    private void atualizarPerfil(
            Usuario usuario,
            DadosAtualizacaoUsuario dados
    ) {

        if (dados.perfilId() == null) {
            return;
        }

        Perfil perfil = buscarPerfilAtivo(
                dados.perfilId()
        );

        usuario.alterarPerfil(
                perfil
        );
    }



    private void atualizarStatus(
            Usuario usuario,
            DadosAtualizacaoUsuario dados
    ) {

        if (dados.ativo() == null) {
            return;
        }

        if (dados.ativo()) {

            if (!usuario.getPerfil().isAtivo()) {

                throw new IllegalArgumentException(
                        "Não é possível ativar o usuário porque o perfil está inativo"
                );
            }

            usuario.ativar();

        } else {

            usuario.desativar();
        }
    }
}