package com.svincent7.sentraiam.identity.service.credential;

import com.svincent7.sentraiam.common.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.CredentialEntity;

public abstract class CredentialService extends BaseService<CredentialEntity, CredentialRequest, CredentialResponse,
        String> {
}
