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

    private static final long MAX_IMAGENS_POR_OS =
            10L;

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
    public OsImagemResponse upload(
            Long ordemServicoId,
            MultipartFile file
    ) {

        validarArquivo(file);
        validarLimiteImagens(ordemServicoId);

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

        boolean uploadRealizado = false;

        try {

            storageService.upload(
                    file.getInputStream(),
                    file.getSize(),
                    storageKey,
                    file.getContentType()
            );

            uploadRealizado = true;

            OsImagem imagem =
                    new OsImagem(
                            ordemServico,
                            storageKey,
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getSize()
                    );

            OsImagem imagemSalva =
                    imagemRepository.save(imagem);

            String urlTemporaria =
                    storageService.gerarUrlTemporaria(
                            imagemSalva.getStorageKey()
                    );

            return OsImagemResponse.from(
                    imagemSalva,
                    urlTemporaria
            );

        } catch (IOException e) {

            if (uploadRealizado) {
                storageService.delete(storageKey);
            }

            throw new RuntimeException(
                    "Erro ao processar imagem",
                    e
            );

        } catch (RuntimeException e) {

            if (uploadRealizado) {
                storageService.delete(storageKey);
            }

            throw e;
        }
    }

    public List<OsImagemResponse> listar(
            Long ordemServicoId
    ) {

        if (!ordemServicoRepository.existsById(ordemServicoId)) {
            throw new IllegalArgumentException(
                    "Ordem de serviço não encontrada"
            );
        }

        return imagemRepository
                .findByOrdemServicoId(ordemServicoId)
                .stream()
                .map(imagem -> {

                    String urlTemporaria =
                            storageService
                                    .gerarUrlTemporaria(
                                            imagem.getStorageKey()
                                    );

                    return OsImagemResponse.from(
                            imagem,
                            urlTemporaria
                    );
                })
                .toList();
    }

    @Transactional
    public void excluir(
            Long ordemServicoId,
            Long imagemId
    ) {

        OsImagem imagem =
                imagemRepository
                        .findByIdAndOrdemServicoId(
                                imagemId,
                                ordemServicoId
                        )
                        .orElseThrow(
                                () -> new IllegalArgumentException(
                                        "Imagem não encontrada para esta ordem de serviço"
                                )
                        );

        storageService.delete(
                imagem.getStorageKey()
        );

        imagemRepository.delete(
                imagem
        );
    }

    private void validarLimiteImagens(
            Long ordemServicoId
    ) {

        long quantidade =
                imagemRepository
                        .countByOrdemServicoId(
                                ordemServicoId
                        );

        if (quantidade >= MAX_IMAGENS_POR_OS) {

            throw new IllegalArgumentException(
                    "Limite máximo de 10 imagens por ordem de serviço"
            );
        }
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