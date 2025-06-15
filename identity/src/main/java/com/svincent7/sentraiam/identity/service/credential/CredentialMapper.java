package com.svincent7.sentraiam.identity.service.credential;

import com.svincent7.sentraiam.common.dto.credential.CredentialRequest;
import com.svincent7.sentraiam.common.dto.credential.CredentialResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.CredentialEntity;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.service.user.UserService;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.util.UUID;

@Getter
@Mapper(componentModel = "spring")
public abstract class CredentialMapper implements BaseMapper<CredentialEntity, CredentialRequest, CredentialResponse> {
    private UserService userService;
    private SecretService secretService;

    @Autowired
    public void setUserService(final UserService injectedUserService) {
        this.userService = injectedUserService;
    }

    @Autowired
    public void setSecretService(final SecretService injectedSecretService) {
        this.secretService = injectedSecretService;
    }

    @Override
    public void updateEntityFromDTO(final CredentialRequest request, final CredentialEntity entity) {
        switch (entity.getType()) {
            case PASSWORD:
                if (entity.getSecretData() != null) {
                    secretService.verifyHashSecretPair(Pair.of(entity.getSecretData(), entity.getCredentialData()),
                            request.getOldPassword());
                }
                Pair<String, String> secrets = secretService.generateHashSecretPair(request.getSecret());
                entity.setSecretData(secrets.getFirst());
                entity.setCredentialData(secrets.getSecond());
                break;
            default:
                throw new IllegalArgumentException("Unsupported entity type: " + entity.getType());
        }
    }

    @Override
    public CredentialEntity toEntity(final CredentialRequest request) {
        if (request == null) {
            return null;
        }
        UserEntity user = userService.getResourceById(request.getUserId());

        CredentialEntity credential = new CredentialEntity();
        credential.setId(UUID.randomUUID().toString());
        credential.setUser(user);
        credential.setUserId(request.getUserId());
        credential.setType(request.getType());
        credential.setRevoked(false);
        switch (request.getType()) {
            case PASSWORD:
                credential.setIdentifier(user.getUsername());
                break;
            default:
                credential.setIdentifier(request.getIdentifier());
                break;
        }
        credential.setVersion(1);
        updateEntityFromDTO(request, credential);

        return credential;
    }
}
