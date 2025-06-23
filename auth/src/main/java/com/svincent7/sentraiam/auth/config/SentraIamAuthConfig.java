package com.svincent7.sentraiam.auth.config;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentraiam.auth.config")
@Data
public class SentraIamAuthConfig implements ConfigProperties {
    private boolean shouldRunInitializer;
    private String sslBundleName;

    private int tokenExpirationMinutes;
    private int jwtKeyRotationHours;
    private int refreshTokenExpirationDays;
    private String defaultKeyAlgorithm;
    private int secretKeyDefaultLength;
}
