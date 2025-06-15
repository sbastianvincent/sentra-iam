package com.svincent7.sentraiam.common.dto.credential;

import lombok.Data;

@Data
public class HashData {
    private SecretData secretData;
    private CredentialData credentialData;
}
