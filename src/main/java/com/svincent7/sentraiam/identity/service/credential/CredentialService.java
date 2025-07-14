package com.svincent7.sentraiam.identity.service.credential;

import com.svincent7.sentraiam.identity.dto.auth.LoginRequest;
import com.svincent7.sentraiam.identity.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.identity.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.identity.dto.credential.CredentialType;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.CredentialEntity;

public abstract class CredentialService extends BaseService<CredentialEntity, CredentialRequest, CredentialResponse,
        String> {
    public abstract VerifyCredentialResponse verifyCredentials(LoginRequest loginRequest);
    public abstract CredentialResponse findByUserIdAndIdentifierAndType(String userId, String username,
                                                                              CredentialType credentialType);
}
