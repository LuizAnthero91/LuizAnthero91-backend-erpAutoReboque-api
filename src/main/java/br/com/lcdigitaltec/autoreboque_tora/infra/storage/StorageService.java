package br.com.lcdigitaltec.autoreboque_tora.infra.storage;

import java.io.InputStream;

public interface StorageService {

    void upload(
            InputStream inputStream,
            long tamanho,
            String key,
            String contentType
    );

    void delete(String key);

    String gerarUrlTemporaria(String key);
}
