package com.svincent7.sentraiam.pki.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.svincent7.sentraiam.pki.config")
@Data
public class PkiConfig {
    private String bootstrapCaToken;
    private boolean forceRegenerateRootCa;
    private boolean skipRootCaCheck;
    private String rootCaPath;
    private String rootCaPassword;
    private String rootCaAlias;
    private int rootCaValidityDays;
    private String rootCaCommonName;
    private int certificateValidityDays;
}
