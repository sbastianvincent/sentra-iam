package com.svincent7.sentraiam.identity.service.jwtkey;

import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import lombok.Data;

@Data
public class JwtKeyResponse {
    private String id;
    private String tenantId;
    private String keyVersion;
    private String privateKey;
    private String publicKey;
    private KeyAlgorithm keyAlgorithm;
    private long createdTimestamp;
    private long expiredTimestamp;
}
