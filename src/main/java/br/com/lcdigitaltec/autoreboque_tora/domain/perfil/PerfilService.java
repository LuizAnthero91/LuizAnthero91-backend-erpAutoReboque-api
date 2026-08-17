package br.com.lcdigitaltec.autoreboque_tora.domain.perfil;

import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosAtualizacaoPerfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosCadastroPerfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.perfil.dto.DadosDetalhamentoPerfil;
import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.Permissao;
import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.PermissaoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PerfilService {

    private final PerfilRepository perfilRepository;
    private final PermissaoRepository permissaoRepository;

    public PerfilService(
            PerfilRepository perfilRepository,
            PermissaoRepository permissaoRepository
    ) {
        this.perfilRepository = perfilRepository;
        this.permissaoRepository = permissaoRepository;
    }

    // =====================================================
    // CADASTRAR
    // =====================================================

    @Transactional
    public DadosDetalhamentoPerfil cadastrar(
            DadosCadastroPerfil dados
    ) {

        validarNomeDuplicado(
                dados.nome()
        );

        Set<Permissao> permissoes =
                buscarPermissoes(
                        dados.permissoes()
                );

        Perfil perfil = new Perfil(
                dados.nome(),
                dados.descricao(),
                permissoes
        );

        perfilRepository.save(perfil);

        return new DadosDetalhamentoPerfil(
                perfil
        );
    }

    // =====================================================
    // LISTAR
    // =====================================================

    @Transactional(readOnly = true)
    public List<DadosDetalhamentoPerfil> listar() {

        return perfilRepository
                .findAll()
                .stream()
                .map(DadosDetalhamentoPerfil::new)
                .toList();
    }

    // =====================================================
    // DETALHAR
    // =====================================================

    @Transactional(readOnly = true)
    public DadosDetalhamentoPerfil detalhar(
            Long id
    ) {

        Perfil perfil = buscarPerfil(id);

        return new DadosDetalhamentoPerfil(
                perfil
        );
    }

    // =====================================================
    // ATUALIZAR
    // =====================================================

    @Transactional
    public DadosDetalhamentoPerfil atualizar(
            Long id,
            DadosAtualizacaoPerfil dados
    ) {

        Perfil perfil = buscarPerfil(id);

        atualizarNome(
                perfil,
                dados
        );

        atualizarDescricao(
                perfil,
                dados
        );

        atualizarPermissoes(
                perfil,
                dados
        );

        atualizarStatus(
                perfil,
                dados
        );

        return new DadosDetalhamentoPerfil(
                perfil
        );
    }

    // =====================================================
    // DESATIVAR
    // =====================================================

    @Transactional
    public void desativar(
            Long id
    ) {

        Perfil perfil = buscarPerfil(id);

        perfil.desativar();
    }

    // =====================================================
    // ATIVAR
    // =====================================================

    @Transactional
    public DadosDetalhamentoPerfil ativar(
            Long id
    ) {

        Perfil perfil = buscarPerfil(id);

        perfil.ativar();

        return new DadosDetalhamentoPerfil(
                perfil
        );
    }

    // =====================================================
    // MÉTODOS PRIVADOS
    // =====================================================

    private Perfil buscarPerfil(
            Long id
    ) {

        return perfilRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Perfil não encontrado"
                        )
                );
    }

    private Set<Permissao> buscarPermissoes(
            Set<Long> ids
    ) {

        if (ids == null || ids.isEmpty()) {

            throw new IllegalArgumentException(
                    "O perfil deve possuir pelo menos uma permissão"
            );
        }

        List<Permissao> permissoesEncontradas =
                permissaoRepository
                        .findAllById(ids);

        if (permissoesEncontradas.size()
                != ids.size()) {

            throw new IllegalArgumentException(
                    "Uma ou mais permissões informadas não existem"
            );
        }

        return new HashSet<>(
                permissoesEncontradas
        );
    }

    private void validarNomeDuplicado(
            String nome
    ) {

        if (perfilRepository
                .existsByNomeIgnoreCase(nome)) {

            throw new IllegalArgumentException(
                    "Já existe um perfil com esse nome"
            );
        }
    }

    private void atualizarNome(
            Perfil perfil,
            DadosAtualizacaoPerfil dados
    ) {

        if (dados.nome() == null) {
            return;
        }

        boolean nomeEmUso =
                perfilRepository
                        .existsByNomeIgnoreCaseAndIdNot(
                                dados.nome(),
                                perfil.getId()
                        );

        if (nomeEmUso) {

            throw new IllegalArgumentException(
                    "Já existe outro perfil com esse nome"
            );
        }

        perfil.alterarNome(
                dados.nome()
        );
    }

    private void atualizarDescricao(
            Perfil perfil,
            DadosAtualizacaoPerfil dados
    ) {

        if (dados.descricao() == null) {
            return;
        }

        perfil.alterarDescricao(
                dados.descricao()
        );
    }

    private void atualizarPermissoes(
            Perfil perfil,
            DadosAtualizacaoPerfil dados
    ) {

        if (dados.permissoes() == null) {
            return;
        }

        Set<Permissao> permissoes =
                buscarPermissoes(
                        dados.permissoes()
                );

        perfil.alterarPermissoes(
                permissoes
        );
    }

    private void atualizarStatus(
            Perfil perfil,
            DadosAtualizacaoPerfil dados
    ) {

        if (dados.ativo() == null) {
            return;
        }

        if (dados.ativo()) {

            perfil.ativar();

        } else {

            perfil.desativar();
        }
    }
}