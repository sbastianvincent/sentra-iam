package com.svincent7.sentraiam.auth.service.jwtkey;

import com.svincent7.sentraiam.auth.model.KeyAlgorithm;
import lombok.Data;

@Data
public class JwtKeyResponse {
    private String id;
    private String tenantId;
    private String keyVersion;
    private String keyValue;
    private KeyAlgorithm keyAlgorithm;
    private long createdTimestamp;
    private long expiredTimestamp;
}
