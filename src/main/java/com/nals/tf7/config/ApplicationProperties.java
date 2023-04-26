package com.nals.tf7.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * Properties specific to Review 360 application
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String timezone;
    private String serviceName;
    private String consoleUrl;

    private long accessTokenValidityInSeconds;
    private long refreshTokenValidityInSeconds;
    private int loginLimitTryNumber;
    private int loginLockedTime;

    private int activationExpiredTime;
    private int activationKeyLimitNumber;
    private int activationKeyLimitTimeAmount;

    private int resetPasswordExpiredTime;
    private int resetPasswordKeyLimitNumber;
    private int resetPasswordKeyLimitTimeAmount;

    private FileUpload fileUpload;
    private AmazonS3 amazonS3;
    private Authentication authentication;
    private Set<String> sensitiveKeywords = new HashSet<>();

    @Getter
    @Setter
    public static class Authentication {
        private int limitTryNumber;
        private int lockedTimeInMinutes;
    }

    @Getter
    @Setter
    public static class FileUpload {
        private int maxSizeAllow = 10240;
        private int allowWidth;
        private int allowHeight;
        private Set<String> allowExtensions = new HashSet<>();
    }

    @Getter
    @Setter
    public static class AmazonS3 {
        private String accessKey = "";
        private String secretKey = "";
        private String region = "";
        private String bucketName = "";
        private String tempDir = "";
        private String workingDir = "";
        private boolean useIamRole = false;
    }
}
