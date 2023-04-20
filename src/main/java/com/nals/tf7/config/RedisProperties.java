package com.nals.tf7.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "redis", ignoreUnknownFields = false)
public class RedisProperties {
    private String hostName;
    private int port;
    private String password;
}
