package br.com.lcdigitaltec.autoreboque_tora.ordemservico.imagem;

import br.com.lcdigitaltec.autoreboque_tora.ordemservico.OrdemServico;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "os_imagens")
public class OsImagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ordem_servico_id",
            nullable = false
    )
    private OrdemServico ordemServico;

    @Column(
            name = "storage_key",
            nullable = false,
            unique = true,
            length = 500
    )
    private String storageKey;

    @Column(
            nullable = false,
            length = 1000
    )
    private String url;

    @Column(
            name = "nome_original",
            length = 255
    )
    private String nomeOriginal;

    @Column(
            name = "content_type",
            length = 100
    )
    private String contentType;

    @Column(
            name = "tamanho_bytes"
    )
    private Long tamanhoBytes;

    @Column(
            name = "criado_em",
            nullable = false
    )
    private LocalDateTime criadoEm;

    protected OsImagem() {
    }

    public OsImagem(
            OrdemServico ordemServico,
            String storageKey,
            String url,
            String nomeOriginal,
            String contentType,
            Long tamanhoBytes
    ) {
        this.ordemServico = ordemServico;
        this.storageKey = storageKey;
        this.url = url;
        this.nomeOriginal = nomeOriginal;
        this.contentType = contentType;
        this.tamanhoBytes = tamanhoBytes;
        this.criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public String getUrl() {
        return url;
    }

    public String getNomeOriginal() {
        return nomeOriginal;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getTamanhoBytes() {
        return tamanhoBytes;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}