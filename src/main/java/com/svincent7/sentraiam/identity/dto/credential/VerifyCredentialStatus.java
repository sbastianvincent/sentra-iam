package com.svincent7.sentraiam.identity.dto.credential;

public enum VerifyCredentialStatus {
    TENANT_NOT_FOUND,
    INACTIVE_TENANT,
    USER_NOT_FOUND,
    INACTIVE_USER,
    INVALID_CREDENTIAL,
    SUCCESS,
}
