package com.svincent7.sentraiam.identity.config;

import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentraiam.identity.config")
@Data
public class IdentityConfig implements ConfigProperties {
    private boolean shouldRunInitializer;
    private String sslBundleName;

    private String masterTenantId;
    private String masterTenantName;
    private String masterUserUsername;
    private String masterUserPassword;
    private String masterRoleName;

    private int tokenExpirationMinutes;
    private int jwtKeyRotationHours;
    private int refreshTokenExpirationDays;
    private String jwtDefaultKeyPairAlgorithm;
    private KeyAlgorithm jwtDefaultKeyAlgorithm;
    private int jwtDefaultKeyLength;
    private String openidConfigurationUri;
}
