package com.nals.tf7.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Slf4j
@Configuration
public class AmazonS3Configuration {
    private final ApplicationProperties.AmazonS3 amazonS3;

    public AmazonS3Configuration(final ApplicationProperties applicationProperties) {
        this.amazonS3 = applicationProperties.getAmazonS3();
    }

    @Bean
    public S3Client s3Client() {
        if (amazonS3.isUseIamRole()) {
            return S3Client.create();
        }

        return S3Client.builder()
                       .region(Region.of(amazonS3.getRegion()))
                       .credentialsProvider(this::awsCredentials)
                       .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        if (amazonS3.isUseIamRole()) {
            return S3Presigner.create();
        }

        return S3Presigner.builder()
                          .region(Region.of(amazonS3.getRegion()))
                          .credentialsProvider(this::awsCredentials)
                          .build();
    }

    private AwsCredentials awsCredentials() {
        return AwsBasicCredentials.create(amazonS3.getAccessKey(), amazonS3.getSecretKey());
    }
}
