package com.flaviorosa.gestao_ativos_api.infrastructure.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        MinioClient client = MinioClient
                .builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        garantirBucketExiste(client);

        return client;
    }

    private void garantirBucketExiste(MinioClient client) {
        try{
            boolean existe = client.bucketExists(
                    BucketExistsArgs
                            .builder()
                            .bucket(bucketName)
                            .build()
            );

            if(!existe){
                client.makeBucket(MakeBucketArgs
                        .builder()
                                .bucket(bucketName)
                        .build());
                log.info("Bucket '{}' criado com sucesso.", bucketName);
            } else {
                log.info("Bucket '{}' já existe.", bucketName);
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao verificar/criar bucket no MinIO: " + e.getMessage(), e);
        }
    }
}
