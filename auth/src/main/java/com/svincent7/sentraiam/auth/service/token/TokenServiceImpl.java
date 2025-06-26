package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.dto.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.auth.repository.RefreshTokenRepository;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.common.crypto.CryptoUtil;
import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.BadRequestException;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenServiceImpl implements TokenService {
    private final SentraIamAuthConfig config;
    private final TokenGenerator tokenGenerator;
    private final JwtKeyService jwtKeyService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AccessToken generateAccessToken(final GenerateAccessTokenRequest request) {
        UserResponse userResponse = request.getUser();
        long currentTime = System.currentTimeMillis();
        String tenantId = userResponse.getTenantId();
        long expirationTime = currentTime + TimeUnit.MINUTES.toMillis(config.getTokenExpirationMinutes());
        JwtKeyResponse jwtKey = jwtKeyService.getTenantActiveJwtKey(tenantId);
        PrivateKey privateKey = CryptoUtil.loadPrivateKey(config.getJwtDefaultKeyPairAlgorithm(),
                jwtKey.getPrivateKey());

        Map<String, Object> additionalData = getExtractAdditionalMapData(request);

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(UUID.randomUUID().toString());
        createTokenRequest.setKeyId(jwtKey.getId());
        createTokenRequest.setSubject(userResponse.getId());
        createTokenRequest.setIssuer(config.getOpenidConfigurationUri() + tenantId);
        createTokenRequest.setIssuedAt(currentTime);
        createTokenRequest.setExpiration(expirationTime);
        createTokenRequest.setPrivateKey(privateKey);
        createTokenRequest.setSignatureAlgorithm(jwtKey.getKeyAlgorithm().getSignatureAlgorithm());
        createTokenRequest.setAdditionalData(additionalData);

        String token = tokenGenerator.generateToken(createTokenRequest);

        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(userResponse.getId());
        accessToken.setTenantId(tenantId);
        accessToken.setCreatedAt(currentTime);
        accessToken.setExpiredAt(expirationTime);
        accessToken.setAccessToken(token);
        return accessToken;
    }

    @Override
    public String generateRefreshToken(final UserResponse userResponse) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + TimeUnit.DAYS.toMillis(config.getRefreshTokenExpirationDays());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userResponse.getId());
        refreshToken.setTenantId(userResponse.getTenantId());
        refreshToken.setCreatedTimestamp(currentTime);
        refreshToken.setExpiredTimestamp(expirationTime);

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        return savedToken.getRefreshToken();
    }

    @Override
    public RefreshToken getResourceByRefreshToken(final String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findById(token);
        if (refreshTokenOptional.isEmpty()) {
            throw new ResourceNotFoundException("Invalid Refresh Token");
        }
        RefreshToken refreshToken = refreshTokenOptional.get();
        if (refreshToken.isExpired()) {
            throw new BadRequestException("Refresh Token has expired");
        }
        return refreshToken;
    }

    @Override
    public void expireRefreshToken(final String token) {
        RefreshToken refreshToken = getResourceByRefreshToken(token);
        refreshToken.setExpiredTimestamp(System.currentTimeMillis());
        refreshTokenRepository.save(refreshToken);
    }

    private Map<String, Object> getExtractAdditionalMapData(final GenerateAccessTokenRequest request) {
        final UserResponse userResponse = request.getUser();
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put(TokenConstant.USERNAME, userResponse.getUsername());
        if (!StringUtils.isEmpty(userResponse.getFirstName())) {
            additionalData.put(TokenConstant.FIRSTNAME, userResponse.getFirstName());
        }
        if (!StringUtils.isEmpty(userResponse.getLastName())) {
            additionalData.put(TokenConstant.LASTNAME, userResponse.getLastName());
        }
        additionalData.put(TokenConstant.VERSION, userResponse.getVersion());
        additionalData.put(TokenConstant.TENANT_ID, userResponse.getTenantId());
        additionalData.put(TokenConstant.SCOPES, request.getPermissions());
        return additionalData;
    }
}
