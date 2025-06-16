package com.svincent7.sentraiam.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentraiam.auth.config")
@Data
public class SentraIamAuthConfig {
    private int tokenExpirationMinutes;
    private int jwtKeyRotationHours;
    private int refreshTokenExpirationDays;
    private String defaultKeyAlgorithm;
    private int secretKeyDefaultLength;
}
