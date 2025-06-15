package com.svincent7.sentraiam.identity.service.credential;

import com.svincent7.sentraiam.common.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.CredentialEntity;
import com.svincent7.sentraiam.identity.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl extends CredentialService {

    private final CredentialRepository credentialRepository;
    private final CredentialMapper credentialMapper;

    @Override
    protected JpaRepository<CredentialEntity, String> getRepository() {
        return credentialRepository;
    }

    @Override
    protected BaseMapper<CredentialEntity, CredentialRequest, CredentialResponse> getMapper() {
        return credentialMapper;
    }
}
