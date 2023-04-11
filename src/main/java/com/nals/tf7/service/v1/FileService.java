package com.nals.tf7.service.v1;

import com.nals.tf7.config.ApplicationProperties;
import com.nals.tf7.errors.FileException;
import com.nals.tf7.helpers.FileHelper;
import com.nals.tf7.helpers.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Service
public class FileService {

    private static final String FILE_NAME_PATTERN = "%s_%s.%s";
    private static final int LENGTH_OF_RANDOM_STRING = 30;
    private static final Long DEFAULT_EXPIRATION_PRE_SIGNED_URL_IN_SECONDS = 300L;

    private final String bucketName;
    private final String tmpDir;
    private final String workingDir;
    private final S3Client s3Client;
    private final int maxSizeAllow;
    private final Set<String> allowExtensions;

    public FileService(final ApplicationProperties applicationProperties,
                       final S3Client s3Client) {
        ApplicationProperties.AmazonS3 amazonS3Config = applicationProperties.getAmazonS3();
        this.bucketName = amazonS3Config.getBucketName();
        this.tmpDir = amazonS3Config.getTempDir();
        this.workingDir = amazonS3Config.getWorkingDir();
        this.s3Client = s3Client;

        ApplicationProperties.FileUpload fileUpload = applicationProperties.getFileUpload();
        this.maxSizeAllow = fileUpload.getMaxSizeAllow();
        this.allowExtensions = fileUpload.getAllowExtensions();
    }

    public void saveFile(final String fileName) {
        log.debug("Save file #{}", fileName);
        saveFile(fileName, allowExtensions);
    }

    public void saveFiles(final Collection<String> fileNames) {
        if (CollectionUtils.isEmpty(fileNames)) {
            return;
        }

        fileNames.forEach(this::saveFile);
    }

    public void replaceFile(final String fileName, final String oldFileName, final Set<String> extensions) {
        log.debug("Replace file #{} to #{}", oldFileName, fileName);

        if (Objects.equals(fileName, oldFileName)) {
            return;
        }

        if (invalidFileName(fileName)) {
            throw new FileException("Invalid file name");
        }

        saveFile(fileName, extensions);

        deleteFile(oldFileName);
    }

    public void replaceFile(final String fileName, final String oldFileName) {
        log.debug("Replace file #{} to #{}", oldFileName, fileName);
        replaceFile(fileName, oldFileName, allowExtensions);
    }

    public void deleteFile(final String fileName) {
        log.debug("Delete file #{}", fileName);
        if (invalidFileName(fileName)) {
            return;
        }

        deleteFile(workingDir, fileName);
    }

    public void deleteFiles(final Collection<String> fileNames) {
        log.debug("Delete files #{}", fileNames);
        if (CollectionUtils.isEmpty(fileNames)) {
            return;
        }

        fileNames.forEach(this::deleteFile);
    }

    public void copyFile(final String originalPath, final String destinationPath) {
        log.debug("Copy file from path #{} to path #{}", originalPath, destinationPath);
        if (invalidFileName(originalPath) || invalidFileName(destinationPath)) {
            return;
        }

        copyFile(workingDir, originalPath, workingDir, destinationPath);
    }

    public String uploadFile(final String fileName, final byte[] fileContent)
        throws IOException {
        log.debug("Upload file #{}", fileName);
        return uploadFile(generateFileName(fileName), tmpDir, fileContent);
    }

    public void validateFile(final MultipartFile uploadFile)
        throws IOException {
        if (Objects.isNull(uploadFile)) {
            throw new FileException("File not found");
        }

        if (StringHelper.isBlank(uploadFile.getOriginalFilename())) {
            throw new FileException("Invalid file name");
        }

        if (uploadFile.isEmpty() || uploadFile.getBytes().length > maxSizeAllow * 1024) {
            throw new FileException("File size is not allow");
        }

        validateExtension(uploadFile.getOriginalFilename(), allowExtensions);
    }

    public String generateFileName(final String originalFilename) {
        return String.format(FILE_NAME_PATTERN,
                             RandomStringUtils.randomAlphanumeric(LENGTH_OF_RANDOM_STRING),
                             Instant.now().toEpochMilli(),
                             FileHelper.getExtension(originalFilename));
    }

    public String getFullFileUrl(final String fileName) {
        if (StringHelper.isBlank(fileName)) {
            return null;
        }

        if (fileName.startsWith("http")) {
            return fileName;
        }

        String protocol;
        try {
            protocol = String.format("%s://", new URL("cloudFrontEndpointUrl").getProtocol());
        } catch (MalformedURLException e) {
            log.warn(e.getMessage());
            return fileName;
        }

        String cdnHost = "cloudFrontEndpointUrl".replace(protocol, EMPTY);
        String fileHost = FileHelper.concatPath(cdnHost, workingDir, fileName.trim());

        return String.format("%s%s", protocol, fileHost);
    }

    private String uploadFile(final String fileName, final String targetDir, final byte[] fileContent) {
        if (invalidFileName(fileName) || Objects.isNull(fileContent)) {
            return null;
        }

        try {
            String key = makeObjectRequestKey(targetDir, fileName);

            PutObjectRequest objectRequest = PutObjectRequest
                .builder()
                .contentLength((long) fileContent.length)
                .contentType(URLConnection.guessContentTypeFromName(fileName))
                .bucket(bucketName)
                .key(key)
                .build();

            s3Client.putObject(objectRequest, RequestBody.fromBytes(fileContent));
        } catch (AwsServiceException | SdkClientException e) {
            throw new FileException("Upload failed", e);
        }

        return fileName;
    }

    private void saveFile(final String fileName, final Set<String> extensions) {
        if (invalidFileName(fileName)) {
            return;
        }

        validateExtension(fileName, extensions);
        moveFile(tmpDir, workingDir, fileName);
    }

    private void moveFile(final String sourceDir, final String destinationDir, final String fileName) {
        // Copy file from temp dir to working dir
        copyFile(sourceDir, fileName, destinationDir, fileName);

        // Delete temp file after copied to working dir
        deleteFile(tmpDir, fileName);
    }

    private void copyFile(final String sourceDir, final String originalPath,
                          final String targetDir, final String destinationPath) {

        try {
            String sourceKey = makeObjectRequestKey(sourceDir, originalPath);
            String destinationKey = makeObjectRequestKey(targetDir, destinationPath);

            CopyObjectRequest request = CopyObjectRequest.builder()
                                                         .sourceBucket(bucketName)
                                                         .sourceKey(sourceKey)
                                                         .destinationBucket(bucketName)
                                                         .destinationKey(destinationKey)
                                                         .build();
            s3Client.copyObject(request);
        } catch (S3Exception e) {
            throw new FileException("Copy failed", e);
        }
    }

    private void deleteFile(final String dir, final String fileName) {
        try {
            String key = makeObjectRequestKey(dir, fileName);

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                                                         .bucket(bucketName)
                                                                         .key(key)
                                                                         .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (AwsServiceException | SdkClientException e) {
            throw new FileException("Delete failed", e);
        }
    }

    private String makeObjectRequestKey(final String dir, final String fileName) {
        String key = StringHelper.isBlank(dir) ? fileName : FileHelper.concatPath(dir, fileName);
        return key.startsWith("/") ? key.substring(1) : key;
    }

    private void validateExtension(final String fileName, final Set<String> extensions) {
        String fileExtension = FileHelper.getExtension(fileName);
        if (extensions.stream().noneMatch(extension -> extension.equalsIgnoreCase(fileExtension))) {
            throw new FileException("Invalid extension");
        }
    }

    private boolean invalidFileName(final String fileName) {
        return StringHelper.isBlank(fileName) || fileName.startsWith("http");
    }
}
