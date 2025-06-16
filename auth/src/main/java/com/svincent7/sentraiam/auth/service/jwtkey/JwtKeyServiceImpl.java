package com.svincent7.sentraiam.auth.service.jwtkey;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.auth.repository.JwtKeyRepository;
import com.svincent7.sentraiam.common.crypto.salt.Salt;
import com.svincent7.sentraiam.common.service.BaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JwtKeyServiceImpl extends JwtKeyService {
    private final SentraIamAuthConfig config;
    private final JwtKeyRepository jwtKeyRepository;
    private final JwtKeyMapper jwtKeyMapper;

    @Override
    protected JpaRepository<JwtKey, String> getRepository() {
        return jwtKeyRepository;
    }

    @Override
    protected BaseMapper<JwtKey, JwtKeyRequest, JwtKeyResponse> getMapper() {
        return jwtKeyMapper;
    }

    @Override
    public JwtKeyResponse getTenantActiveJwtKey(final String tenantId) {
        Optional<JwtKey> jwtKeyOptional = jwtKeyRepository.findFirstByTenantIdOrderByKeyVersionDesc(tenantId);
        if (jwtKeyOptional.isPresent()) {
            JwtKey jwtKey = jwtKeyOptional.get();
            if (!jwtKey.isExpired()) {
                return jwtKeyMapper.toResponseDTO(jwtKey);
            }
        }
        return generateJwtKey(tenantId);
    }

    @Override
    public JwtKeyResponse generateJwtKey(final String tenantId) {
        long expiration = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(config.getJwtKeyRotationHours());
        byte[] salt = Salt.generateSalt(config.getSecretKeyDefaultLength());
        String secretKey = Base64.getEncoder().encodeToString(salt);

        JwtKeyRequest jwtKeyRequest = new JwtKeyRequest();
        jwtKeyRequest.setTenantId(tenantId);
        jwtKeyRequest.setKeyVersion(generateKeyVersionFromDate(LocalDateTime.now()));
        jwtKeyRequest.setKeyAlgorithm(config.getDefaultKeyAlgorithm());
        jwtKeyRequest.setKeyValue(secretKey);
        jwtKeyRequest.setExpiredTimestamp(expiration);
        return create(jwtKeyRequest);
    }

    private String generateKeyVersionFromDate(final LocalDateTime date) {
        // Define the formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        // Format the LocalDateTime to string
        return date.format(formatter);
    }
}
