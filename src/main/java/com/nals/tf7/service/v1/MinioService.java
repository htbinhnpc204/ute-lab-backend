package com.nals.tf7.service.v1;

import com.nals.tf7.config.MinioProperties;
import com.nals.tf7.errors.FileException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class MinioService {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioService(final MinioClient minioClient,
                        final MinioProperties minioProperties) {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    public String uploadFile(final String fileName, final InputStream stream, final Long size) {
        log.info("Upload file name #{}", fileName);

        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                             .bucket(minioProperties.getBucketName())
                             .object(fileName)
                             .stream(stream, size, -1)
                             .build());
        } catch (Exception e) {
            throw new FileException("Upload failed: ", e);
        }

        return fileName;
    }
}
