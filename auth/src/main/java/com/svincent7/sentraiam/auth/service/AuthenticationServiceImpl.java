package com.svincent7.sentraiam.auth.service;

import com.svincent7.sentraiam.auth.client.SentraIamIdentityClient;
import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.dto.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.auth.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.auth.service.token.TokenService;
import com.svincent7.sentraiam.common.crypto.CryptoUtil;
import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.auth.LoginResponse;
import com.svincent7.sentraiam.common.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.common.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialStatus;
import com.svincent7.sentraiam.common.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.AuthenticationException;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final SentraIamIdentityClient sentraIamIdentityClient;
    private final TokenService tokenService;
    private final JwtKeyService jwtKeyService;
    private final SentraIamAuthConfig config;

    @Override
    public LoginResponse authenticate(final LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getTenantId()) && StringUtils.isEmpty(loginRequest.getTenantName())) {
            throw new AuthenticationException("Tenant id or Tenant name are required");
        }

        ResponseEntity<VerifyCredentialResponse> response = sentraIamIdentityClient.verifyCredentials(loginRequest);
        VerifyCredentialResponse credentialResponse = response.getBody();
        if (credentialResponse == null || !credentialResponse.getStatus().equals(VerifyCredentialStatus.SUCCESS)
                || credentialResponse.getUser() == null) {
            log.info("Credential Response: {}", credentialResponse);
            throw new AuthenticationException("Invalid credentials");
        }

        return generateLoginResponse(credentialResponse.getUser());
    }

    @Override
    public LoginResponse refresh(final RefreshRequest request) {
        RefreshToken refreshToken = tokenService.getResourceByRefreshToken(request.getRefreshToken());
        ResponseEntity<UserResponse> response = sentraIamIdentityClient.getUser(refreshToken.getUserId());
        UserResponse user = response.getBody();

        if (user == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        if (!user.isEnabled()) {
            throw new UnauthorizedException("Identity is not active. Please contact Administrator");
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
        ResponseEntity<List<RoleWithPermissions>> roleEntity = sentraIamIdentityClient
                .getRoleWithPermissionsByUserId(userResponse.getId());
        List<RoleWithPermissions> roleWithPermissions = roleEntity.getBody();
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
