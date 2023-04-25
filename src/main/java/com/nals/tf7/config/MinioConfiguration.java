package com.nals.tf7.config;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MinioConfiguration {
    private final MinioProperties minioProperties;

    public MinioConfiguration(final MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                          .endpoint(minioProperties.getEndpoint())
                          .credentials(minioProperties.getAccessKey(),
                                       minioProperties.getSecretKey())
                          .build();
    }
}
