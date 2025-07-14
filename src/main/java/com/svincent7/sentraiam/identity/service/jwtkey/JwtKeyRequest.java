package com.svincent7.sentraiam.identity.service.jwtkey;

import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JwtKeyRequest {

    @NotBlank(message = "tenantId is required")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$", message = "tenantId must be valid")
    private String tenantId;

    @NotBlank(message = "keyVersion is required")
    @Pattern(regexp = "^[0-9]+$", message = "keyVersion must contain only numeric")
    private String keyVersion;

    @NotBlank(message = "privateKey is required")
    private String privateKey;

    @NotBlank(message = "publicKey is required")
    private String publicKey;

    @NotBlank(message = "keyAlgorithm is required")
    private KeyAlgorithm keyAlgorithm;

    @NotBlank(message = "keyLength is required")
    private int keyLength;

    @NotBlank(message = "expiredTimestamp is required")
    private long expiredTimestamp;
}
