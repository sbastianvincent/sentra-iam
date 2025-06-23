package com.svincent7.sentraiam.apigateway.config;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentraiam.apigateway.config")
@Data
public class ApiGatewayConfig implements ConfigProperties {
    private boolean shouldRunInitializer;
    private String sslBundleName;
}
