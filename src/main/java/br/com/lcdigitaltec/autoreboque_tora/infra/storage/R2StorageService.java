package br.com.lcdigitaltec.autoreboque_tora.infra.storage;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;

@Service
public class R2StorageService implements StorageService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final R2Properties properties;

    public R2StorageService(
            S3Client s3Client,
            S3Presigner s3Presigner,
            R2Properties properties
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.properties = properties;
    }

    @Override
    public void upload(
            InputStream inputStream,
            long tamanho,
            String key,
            String contentType
    ) {

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucket(properties.bucket())
                        .key(key)
                        .contentType(contentType)
                        .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(
                        inputStream,
                        tamanho
                )
        );
    }

    @Override
    public void delete(
            String key
    ) {

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(properties.bucket())
                        .key(key)
                        .build();

        s3Client.deleteObject(request);
    }

    @Override
    public String gerarUrlTemporaria(
            String key
    ) {

        GetObjectRequest getObjectRequest =
                GetObjectRequest.builder()
                        .bucket(properties.bucket())
                        .key(key)
                        .build();

        GetObjectPresignRequest presignRequest =
                GetObjectPresignRequest.builder()
                        .signatureDuration(
                                Duration.ofMinutes(5)
                        )
                        .getObjectRequest(
                                getObjectRequest
                        )
                        .build();

        return s3Presigner
                .presignGetObject(
                        presignRequest
                )
                .url()
                .toString();
    }
}