package com.svincent7.sentraiam.common.dto.credential;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CredentialRequest {
    @NotNull
    private CredentialType type;
    @NotNull
    private String userId;
    private String identifier;
    @NotNull
    private String secret;
    private String oldPassword;
}
