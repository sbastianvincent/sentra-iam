package com.svincent7.sentraiam.identity.service;

import com.svincent7.sentraiam.common.exception.AuthenticationException;
import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.crypto.CryptoUtil;
import com.svincent7.sentraiam.identity.dto.auth.LoginRequest;
import com.svincent7.sentraiam.identity.dto.auth.LoginResponse;
import com.svincent7.sentraiam.identity.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.identity.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.identity.dto.credential.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialStatus;
import com.svincent7.sentraiam.identity.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.identity.dto.user.UserResponse;
import com.svincent7.sentraiam.identity.model.AccessToken;
import com.svincent7.sentraiam.identity.model.RefreshToken;
import com.svincent7.sentraiam.identity.service.credential.CredentialService;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.identity.service.token.TokenService;
import com.svincent7.sentraiam.identity.service.user.UserService;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final TokenService tokenService;
    private final JwtKeyService jwtKeyService;
    private final IdentityConfig config;
    private final CredentialService credentialService;
    private final UserService userService;

    @Override
    public LoginResponse authenticate(final LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getTenantId()) && StringUtils.isEmpty(loginRequest.getTenantName())) {
            throw new AuthenticationException("Tenant id or Tenant name are required");
        }

        VerifyCredentialResponse credentialResponse = credentialService.verifyCredentials(loginRequest);
        if (credentialResponse == null || credentialResponse.getUser() == null
                || !credentialResponse.getStatus().equals(VerifyCredentialStatus.SUCCESS)) {
            log.info("Credential Response: {}", credentialResponse);
            throw new AuthenticationException("Invalid credentials");
        }

        return generateLoginResponse(credentialResponse.getUser());
    }

    @Override
    public LoginResponse refresh(final RefreshRequest request) {
        RefreshToken refreshToken = tokenService.getResourceByRefreshToken(request.getRefreshToken());
        UserResponse user = userService.getById(refreshToken.getUserId());
        if (user == null) {
            throw new AuthenticationException("Invalid refresh token");
        }

        if (!user.isEnabled()) {
            throw new AuthenticationException("Identity is not active. Please contact Administrator");
        }
        return generateLoginResponse(user);
    }

    @Override
    public void logout(final LogoutRequest logoutRequest) {
        tokenService.expireRefreshToken(logoutRequest.getRefreshToken());
    }

    @Override
    public List<Object> getJwks(final String tenantId) {
        JwtKeyResponse jwtKeyResponse = jwtKeyService.getTenantActiveJwtKey(tenantId);
        RSAPublicKey publicKey = (RSAPublicKey) CryptoUtil.loadPublicKey(config.getJwtDefaultKeyPairAlgorithm(),
                jwtKeyResponse.getPublicKey());
        String n = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray());
        String e = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray());

        Map<String, Object> jwk = Map.of(
                "kty", config.getJwtDefaultKeyPairAlgorithm(),
                "kid", jwtKeyResponse.getId(),
                "alg", jwtKeyResponse.getKeyAlgorithm().name(),
                "use", "sig",
                "n", n,
                "e", e
        );
        return List.of(jwk);
    }

    private LoginResponse generateLoginResponse(final UserResponse userResponse) {
        List<RoleWithPermissions> roleWithPermissions = userService
                .getRoleWithPermissionsByUserId(userResponse.getId());
        log.info("roleWithPermissios: {}", roleWithPermissions);
        GenerateAccessTokenRequest request = new GenerateAccessTokenRequest();
        request.setUser(userResponse);
        request.setRoles(roleWithPermissions);
        AccessToken accessToken = tokenService.generateAccessToken(request);
        String refreshToken = tokenService.generateRefreshToken(userResponse);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken.getAccessToken());
        loginResponse.setUserId(userResponse.getId());
        loginResponse.setTenantId(userResponse.getTenantId());
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresAt(accessToken.getExpiredAt());
        return loginResponse;
    }
}
