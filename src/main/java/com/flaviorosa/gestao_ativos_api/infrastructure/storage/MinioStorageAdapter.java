package com.flaviorosa.gestao_ativos_api.infrastructure.storage;

import com.flaviorosa.gestao_ativos_api.application.port.StoragePort;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinioStorageAdapter implements StoragePort {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Override
    public String upload(String nomeArquivo, InputStream inputStream, long tamanhoBytes, String contentTypes) {

        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(nomeArquivo)
                            .stream(inputStream, tamanhoBytes, -1)
                            .contentType(contentTypes)
                            .build()
            );

            log.info("Arquivo '{}' enviado para o MinIO.", nomeArquivo);

            return geraUrlTemporaria(nomeArquivo, 60 * 24 * 7); // URL válida por 7 dias
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao fazer upload do arquivo no MinIO: " + e.getMessage(), e
            );
        }

    }

    @Override
    public void deletar(String nomeArquivo) {

        try {
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(nomeArquivo)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao deletar arquivo no MinIO: " + e.getMessage(), e
            );
        }
    }

    @Override
    public String geraUrlTemporaria(String nomeArquivo, int expiracaoMinutos) {

        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .bucket(bucketName)
                            .object(nomeArquivo)
                            .method(Method.GET)
                            .expiry(expiracaoMinutos, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao gerar URL temporária no MinIO: " + e.getMessage(), e
            );
        }
    }

    @Override
    public void garantirBucketExiste() {
        // Delegado ao MinioConfig na inicialização da aplicação
        log.info("Bucket verificado pelo MinioConfig na inicialização.");
    }
}
