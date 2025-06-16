package com.svincent7.sentraiam.identity.service.credential;

import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.common.dto.credential.CredentialType;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialStatus;
import com.svincent7.sentraiam.common.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.common.dto.tenant.TenantStatus;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.CredentialEntity;
import com.svincent7.sentraiam.identity.repository.CredentialRepository;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import com.svincent7.sentraiam.identity.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CredentialServiceImpl extends CredentialService {

    private final TenantService tenantService;
    private final UserService userService;
    private final SecretService secretService;
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

    @Override
    public VerifyCredentialResponse verifyCredentials(final LoginRequest loginRequest) {
        final TenantResponse tenant;
        if (!StringUtils.isEmpty(loginRequest.getTenantId())) {
            try {
                tenant = tenantService.getById(loginRequest.getTenantId());
            } catch (ResourceNotFoundException exception) {
                return returnResponseWithStatus(VerifyCredentialStatus.TENANT_NOT_FOUND);
            }
        } else if (!StringUtils.isEmpty(loginRequest.getTenantName())) {
            try {
                tenant = tenantService.getTenantByName(loginRequest.getTenantName());
            } catch (ResourceNotFoundException exception) {
                return returnResponseWithStatus(VerifyCredentialStatus.TENANT_NOT_FOUND);
            }
        } else {
            return returnResponseWithStatus(VerifyCredentialStatus.TENANT_NOT_FOUND);
        }

        if (tenant.getTenantStatus() != TenantStatus.ACTIVE) {
            return returnResponseWithStatus(VerifyCredentialStatus.INACTIVE_TENANT);
        }

        final UserResponse user;
        try {
            user = userService.getByUsernameAndTenantId(loginRequest.getUsername(), tenant.getId());
        } catch (ResourceNotFoundException exception) {
            return returnResponseWithStatus(VerifyCredentialStatus.USER_NOT_FOUND);
        }

        if (!user.isEnabled()) {
            return returnResponseWithStatus(VerifyCredentialStatus.INACTIVE_USER);
        }

        Optional<CredentialEntity> credentialEntityOptional = credentialRepository
                .findByUserIdAndIdentifierAndType(user.getId(), loginRequest.getUsername(), CredentialType.PASSWORD);
        if (credentialEntityOptional.isEmpty()) {
            return returnResponseWithStatus(VerifyCredentialStatus.INVALID_CREDENTIAL);
        }

        CredentialEntity credentialEntity = credentialEntityOptional.get();
        if (credentialEntity.isRevoked() || StringUtils.isEmpty(credentialEntity.getSecretData())
                || StringUtils.isEmpty(credentialEntity.getCredentialData())) {
            return returnResponseWithStatus(VerifyCredentialStatus.INVALID_CREDENTIAL);
        }

        if (!secretService.verifyHashSecretPair(Pair.of(credentialEntity.getSecretData(),
                        credentialEntity.getCredentialData()), loginRequest.getPassword())) {
            return returnResponseWithStatus(VerifyCredentialStatus.INVALID_CREDENTIAL);
        }

        VerifyCredentialResponse verifyCredentialResponse = new VerifyCredentialResponse();
        verifyCredentialResponse.setUser(user);
        verifyCredentialResponse.setStatus(VerifyCredentialStatus.SUCCESS);
        return verifyCredentialResponse;
    }

    @Override
    public CredentialResponse findByUserIdAndIdentifierAndType(final String userId,
                                                                     final String username,
                                                                     final CredentialType credentialType) {
        CredentialEntity credentialEntity = credentialRepository.findByUserIdAndIdentifierAndType(
                userId, username, credentialType).orElseThrow(
                        () -> new ResourceNotFoundException("Resource with User ID: " + userId + ", username: "
                                + username + " and credentialType: " + credentialType + " is not found"));
        return getMapper().toResponseDTO(credentialEntity);
    }

    private VerifyCredentialResponse returnResponseWithStatus(final VerifyCredentialStatus status) {
        VerifyCredentialResponse verifyCredentialResponse = new VerifyCredentialResponse();
        verifyCredentialResponse.setStatus(status);
        return verifyCredentialResponse;
    }
}
