package com.svincent7.sentraiam.auth.service.jwtkey;

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

    @NotBlank(message = "keyValue is required")
    private String keyValue;

    @NotBlank(message = "keyAlgorithm is required")
    private String keyAlgorithm;

    @NotBlank(message = "expiredTimestamp is required")
    private long expiredTimestamp;
}
