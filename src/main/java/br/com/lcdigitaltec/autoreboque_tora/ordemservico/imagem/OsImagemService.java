package br.com.lcdigitaltec.autoreboque_tora.ordemservico.imagem;

import br.com.lcdigitaltec.autoreboque_tora.infra.storage.StorageService;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServico;
import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServicoRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class OsImagemService {

    private static final long MAX_FILE_SIZE =
            8L * 1024 * 1024;

    private static final Set<String> TIPOS_PERMITIDOS =
            Set.of(
                    "image/jpeg",
                    "image/png",
                    "image/webp"
            );

    private final StorageService storageService;

    private final OsImagemRepository imagemRepository;

    private final OrdemServicoRepository ordemServicoRepository;

    public OsImagemService(
            StorageService storageService,
            OsImagemRepository imagemRepository,
            OrdemServicoRepository ordemServicoRepository
    ) {
        this.storageService = storageService;
        this.imagemRepository = imagemRepository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    @Transactional
    public OsImagem upload(
            Long ordemServicoId,
            MultipartFile file
    ) {

        validarArquivo(file);

        OrdemServico ordemServico =
                ordemServicoRepository
                        .findById(ordemServicoId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Ordem de serviço não encontrada"
                                )
                        );

        String extensao =
                obterExtensao(
                        file.getContentType()
                );

        String nomeArquivo =
                UUID.randomUUID()
                        + "."
                        + extensao;

        String storageKey =
                "erp/os/"
                        + ordemServicoId
                        + "/"
                        + nomeArquivo;

        try {

            String url =
                    storageService.upload(
                            file.getInputStream(),
                            file.getSize(),
                            storageKey,
                            file.getContentType()
                    );

            OsImagem imagem =
                    new OsImagem(
                            ordemServico,
                            storageKey,
                            url,
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getSize()
                    );

            return imagemRepository.save(imagem);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao processar imagem",
                    e
            );
        }
    }

    public List<OsImagem> listar(
            Long ordemServicoId
    ) {

        return imagemRepository
                .findByOrdemServicoId(
                        ordemServicoId
                );
    }

    @Transactional
    public void excluir(
            Long ordemServicoId,
            Long imagemId
    ) {

        OsImagem imagem =
                imagemRepository
                        .findById(imagemId)
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Imagem não encontrada"
                                )
                        );

        if (!imagem
                .getOrdemServico()
                .getId()
                .equals(ordemServicoId)) {

            throw new IllegalArgumentException(
                    "Imagem não pertence a esta ordem de serviço"
            );
        }

        storageService.delete(
                imagem.getStorageKey()
        );

        imagemRepository.delete(
                imagem
        );
    }

    private void validarArquivo(
            MultipartFile file
    ) {

        if (file == null ||
                file.isEmpty()) {

            throw new IllegalArgumentException(
                    "Arquivo não informado"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {

            throw new IllegalArgumentException(
                    "Imagem deve possuir no máximo 8 MB"
            );
        }

        String contentType =
                file.getContentType();

        if (contentType == null ||
                !TIPOS_PERMITIDOS.contains(
                        contentType
                )) {

            throw new IllegalArgumentException(
                    "Formato de imagem não permitido"
            );
        }

        validarConteudoImagem(file);
    }

    private void validarConteudoImagem(
            MultipartFile file
    ) {

        if ("image/webp".equals(
                file.getContentType()
        )) {
            return;
        }

        try {

            BufferedImage imagem =
                    ImageIO.read(
                            file.getInputStream()
                    );

            if (imagem == null) {

                throw new IllegalArgumentException(
                        "Arquivo enviado não é uma imagem válida"
                );
            }

        } catch (IOException e) {

            throw new IllegalArgumentException(
                    "Não foi possível validar a imagem",
                    e
            );
        }
    }

    private String obterExtensao(
            String contentType
    ) {

        return switch (contentType) {

            case "image/jpeg" -> "jpg";

            case "image/png" -> "png";

            case "image/webp" -> "webp";

            default ->
                    throw new IllegalArgumentException(
                            "Tipo de imagem inválido"
                    );
        };
    }
}