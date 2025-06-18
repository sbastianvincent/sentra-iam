package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.auth.repository.RefreshTokenRepository;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.common.auth.token.SentraClaims;
import com.svincent7.sentraiam.common.auth.token.TokenUtils;
import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.BadRequestException;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
    public AccessToken generateAccessToken(final UserResponse userResponse) {
        long currentTime = System.currentTimeMillis();
        long expirationTime = currentTime + TimeUnit.MINUTES.toMillis(config.getTokenExpirationMinutes());
        JwtKeyResponse jwtKey = jwtKeyService.getTenantActiveJwtKey(userResponse.getTenantId());

        Map<String, Object> additionalData = getExtractAdditionalMapData(userResponse);

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(UUID.randomUUID().toString());
        createTokenRequest.setKeyId(jwtKey.getId());
        createTokenRequest.setSubject(userResponse.getId());
        createTokenRequest.setIssuer(userResponse.getTenantId());
        createTokenRequest.setIssuedAt(currentTime);
        createTokenRequest.setExpiration(expirationTime);
        createTokenRequest.setSecret(jwtKey.getKeyValue());
        createTokenRequest.setSignatureAlgorithm(jwtKey.getKeyAlgorithm().getSignatureAlgorithm());
        createTokenRequest.setAdditionalData(additionalData);

        String token = tokenGenerator.generateToken(createTokenRequest);

        AccessToken accessToken = new AccessToken();
        accessToken.setUserId(userResponse.getId());
        accessToken.setTenantId(userResponse.getTenantId());
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

    @Override
    public SentraClaims authenticate(final String token) {
        JSONObject header = TokenUtils.parseHeaderFromToken(token);
        JSONObject payload = TokenUtils.parsePayloadFromToken(token);
        String tenantId = payload.getString(Claims.ISSUER);
        String keyId = header.getString(TokenConstant.KEY_ID);
        JwtKey jwtKey = jwtKeyService.getResourceById(keyId);
        log.debug("authenticate::jwtKey {}", jwtKey);
        if (jwtKey.isExpired()) {
            throw new UnauthorizedException("JWT Key expired. Please refresh your token");
        }

        if (!jwtKey.getTenantId().equals(tenantId)) {
            throw new UnauthorizedException("Invalid Issuer");
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey.getKeyValue()).parseClaimsJws(token).getBody();
            SentraClaims sentraClaims = new SentraClaims(claims, payload);

            log.debug("authenticate::sentraClaims {}", sentraClaims);
            return sentraClaims;
        } catch (Exception e) {
            log.error("Got Exception when parsing claims: ", e);
            throw new UnauthorizedException("Invalid Token Signature");
        }
    }

    private Map<String, Object> getExtractAdditionalMapData(final UserResponse userResponse) {
        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put(TokenConstant.USERNAME, userResponse.getUsername());
        if (!StringUtils.isEmpty(userResponse.getFirstName())) {
            additionalData.put(TokenConstant.FIRSTNAME, userResponse.getFirstName());
        }
        if (!StringUtils.isEmpty(userResponse.getLastName())) {
            additionalData.put(TokenConstant.LASTNAME, userResponse.getLastName());
        }
        additionalData.put(TokenConstant.VERSION, userResponse.getVersion());
        return additionalData;
    }
}
