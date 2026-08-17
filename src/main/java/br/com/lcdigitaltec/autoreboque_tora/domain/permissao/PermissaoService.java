package br.com.lcdigitaltec.autoreboque_tora.domain.permissao;

import br.com.lcdigitaltec.autoreboque_tora.domain.permissao.dto.DadosDetalhamentoPermissao;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissaoService {

    private final PermissaoRepository permissaoRepository;

    public PermissaoService(
            PermissaoRepository permissaoRepository
    ) {
        this.permissaoRepository = permissaoRepository;
    }


    @Transactional(readOnly = true)
    public List<DadosDetalhamentoPermissao> listar() {

        return permissaoRepository
                .findAllByOrderByCodigoAsc()
                .stream()
                .map(DadosDetalhamentoPermissao::new)
                .toList();
    }


    @Transactional(readOnly = true)
    public DadosDetalhamentoPermissao detalhar(
            Long id
    ) {

        Permissao permissao =
                permissaoRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Permissão não encontrada"
                                )
                        );

        return new DadosDetalhamentoPermissao(
                permissao
        );
    }
}