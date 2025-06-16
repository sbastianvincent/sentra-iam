package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.auth.repository.RefreshTokenRepository;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.BadRequestException;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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

        Map<String, Object> additionalData = new HashMap<>();
        additionalData.put(TokenConstant.USERNAME, userResponse.getUsername());
        additionalData.put(TokenConstant.FIRSTNAME, userResponse.getFirstName());
        additionalData.put(TokenConstant.LASTNAME, userResponse.getLastName());
        additionalData.put(TokenConstant.VERSION, userResponse.getVersion());

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setId(jwtKey.getId());
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
    public UserResponse authenticate(final String token) {
        JSONObject payload = parsePayloadFromToken(token);
        String keyId = payload.getString(Claims.ID);
        JwtKey jwtKey = jwtKeyService.getResourceById(keyId);
        log.debug("authenticate::jwtKey {}", jwtKey);
        if (jwtKey.isExpired()) {
            throw new UnauthorizedException("JWT Key expired. Please refresh your token");
        }
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtKey.getKeyValue()).parseClaimsJws(token).getBody();
            log.debug("authenticate::claims {}", claims);
            return UserResponse.fromClaimsAndPayload(claims, payload);
        } catch (Exception e) {
            log.error("Got Exception when parsing claims: ", e);
            throw new UnauthorizedException("Invalid Token Signature");
        }
    }

    @Override
    public void claimAccessToken(final String token) {
        try {
            Authentication authentication = authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("Got Exception when parsing claims: ", e);
            throw new UnauthorizedException("Invalid Token Signature");
        }
    }

    private JSONObject parsePayloadFromToken(final String token) throws UnauthorizedException {
        String[] parts = token.split("\\.");
        if (parts.length != TokenConstant.TOTAL_TOKEN_INDEX) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        JSONObject payload = new JSONObject(decode(parts[TokenConstant.TOKEN_PAYLOAD_INDEX]));
        int expiration = payload.getInt(Claims.EXPIRATION);
        String tenantId = payload.getString(Claims.ISSUER);

        if (tenantId == null || expiration == 0) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        if (Instant.ofEpochSecond(expiration).isBefore(Instant.now())) {
            throw new UnauthorizedException("Token is expired");
        }

        return payload;
    }

    private String decode(final String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
