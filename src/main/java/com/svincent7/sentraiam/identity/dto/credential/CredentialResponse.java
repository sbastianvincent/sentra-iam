package com.svincent7.sentraiam.identity.dto.credential;

import lombok.Data;

@Data
public class CredentialResponse {
    private String id;
    private CredentialType type;
    private String userId;
    private String identifier;
    private String secretData;
    private boolean revoked;
    private int version;
}
