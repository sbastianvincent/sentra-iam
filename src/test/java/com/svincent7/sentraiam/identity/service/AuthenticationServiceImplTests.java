package com.svincent7.sentraiam.identity.service;

import com.svincent7.sentraiam.common.exception.AuthenticationException;
import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.crypto.CryptoUtil;
import com.svincent7.sentraiam.identity.dto.auth.LoginRequest;
import com.svincent7.sentraiam.identity.dto.auth.LoginResponse;
import com.svincent7.sentraiam.identity.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.identity.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.identity.dto.credential.VerifyCredentialStatus;
import com.svincent7.sentraiam.identity.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.identity.dto.user.UserResponse;
import com.svincent7.sentraiam.identity.model.AccessToken;
import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import com.svincent7.sentraiam.identity.model.RefreshToken;
import com.svincent7.sentraiam.identity.service.credential.CredentialService;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyService;
import com.svincent7.sentraiam.identity.service.token.TokenService;
import com.svincent7.sentraiam.identity.service.user.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class AuthenticationServiceImplTests {

    private TokenService tokenService;
    private JwtKeyService jwtKeyService;
    private IdentityConfig config;
    private AuthenticationService authenticationService;
    private CredentialService credentialService;
    private UserService userService;

    @BeforeEach
    void setup() {
        tokenService = Mockito.mock(TokenService.class);
        jwtKeyService = Mockito.mock(JwtKeyService.class);
        config = Mockito.mock(IdentityConfig.class);
        credentialService = Mockito.mock(CredentialService.class);
        userService = Mockito.mock(UserService.class);
        authenticationService = new AuthenticationServiceImpl(tokenService, jwtKeyService, config, credentialService, userService);
    }

    @Test
    void testAuthenticate() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn("test-tenant");

        VerifyCredentialResponse verifyCredentialResponse = Mockito.mock(VerifyCredentialResponse.class);
        Mockito.when(credentialService.verifyCredentials(loginRequest)).thenReturn(verifyCredentialResponse);
        Mockito.when(verifyCredentialResponse.getStatus()).thenReturn(VerifyCredentialStatus.SUCCESS);

        UserResponse userResponse = Mockito.mock(UserResponse.class);
        Mockito.when(userResponse.getId()).thenReturn("user-id");
        Mockito.when(userResponse.getTenantId()).thenReturn("tenant-id");
        Mockito.when(verifyCredentialResponse.getUser()).thenReturn(userResponse);

        List<RoleWithPermissions> roleWithPermissions = new ArrayList<>();
        roleWithPermissions.add(Mockito.mock(RoleWithPermissions.class));
        Mockito.when(userService.getRoleWithPermissionsByUserId("user-id")).thenReturn(roleWithPermissions);

        long expiredAt = System.currentTimeMillis() + 10000;

        AccessToken accessToken = Mockito.mock(AccessToken.class);
        Mockito.when(accessToken.getAccessToken()).thenReturn("access-token");
        Mockito.when(accessToken.getExpiredAt()).thenReturn(expiredAt);
        Mockito.when(tokenService.generateAccessToken(Mockito.any())).thenReturn(accessToken);
        Mockito.when(tokenService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        LoginResponse result = authenticationService.authenticate(loginRequest);

        Assertions.assertEquals(result.getAccessToken(), "access-token");
        Assertions.assertEquals(result.getUserId(), "user-id");
        Assertions.assertEquals(result.getTenantId(), "tenant-id");
        Assertions.assertEquals(result.getRefreshToken(), "refresh-token");
        Assertions.assertEquals(result.getExpiresAt(), expiredAt);
    }

    @Test
    void testAuthenticate_LoginRequestNullTenantId() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn(null);
        Mockito.when(loginRequest.getTenantName()).thenReturn("tenant-name");

        VerifyCredentialResponse verifyCredentialResponse = Mockito.mock(VerifyCredentialResponse.class);
        Mockito.when(credentialService.verifyCredentials(loginRequest)).thenReturn(verifyCredentialResponse);
        Mockito.when(verifyCredentialResponse.getStatus()).thenReturn(VerifyCredentialStatus.SUCCESS);

        UserResponse userResponse = Mockito.mock(UserResponse.class);
        Mockito.when(userResponse.getId()).thenReturn("user-id");
        Mockito.when(userResponse.getTenantId()).thenReturn("tenant-id");
        Mockito.when(verifyCredentialResponse.getUser()).thenReturn(userResponse);

        List<RoleWithPermissions> roleWithPermissions = new ArrayList<>();
        roleWithPermissions.add(Mockito.mock(RoleWithPermissions.class));
        Mockito.when(userService.getRoleWithPermissionsByUserId("user-id")).thenReturn(roleWithPermissions);

        long expiredAt = System.currentTimeMillis() + 10000;

        AccessToken accessToken = Mockito.mock(AccessToken.class);
        Mockito.when(accessToken.getAccessToken()).thenReturn("access-token");
        Mockito.when(accessToken.getExpiredAt()).thenReturn(expiredAt);
        Mockito.when(tokenService.generateAccessToken(Mockito.any())).thenReturn(accessToken);
        Mockito.when(tokenService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        LoginResponse result = authenticationService.authenticate(loginRequest);

        Assertions.assertEquals(result.getAccessToken(), "access-token");
        Assertions.assertEquals(result.getUserId(), "user-id");
        Assertions.assertEquals(result.getTenantId(), "tenant-id");
        Assertions.assertEquals(result.getRefreshToken(), "refresh-token");
        Assertions.assertEquals(result.getExpiresAt(), expiredAt);
    }

    @Test
    void testAuthenticate_LoginRequestNullTenantIdAndTenantName_ThrowsException() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn(null);
        Mockito.when(loginRequest.getTenantName()).thenReturn(null);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(loginRequest));
    }

    @Test
    void testAuthenticate_VerifyCredentialResponseNull() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn("test-tenant");

        Mockito.when(credentialService.verifyCredentials(loginRequest)).thenReturn(null);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(loginRequest));
    }

    @Test
    void testAuthenticate_VerifyCredentialResponseUserNull() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn("test-tenant");

        VerifyCredentialResponse verifyCredentialResponse = Mockito.mock(VerifyCredentialResponse.class);
        Mockito.when(credentialService.verifyCredentials(loginRequest)).thenReturn(verifyCredentialResponse);
        Mockito.when(verifyCredentialResponse.getStatus()).thenReturn(VerifyCredentialStatus.SUCCESS);
        Mockito.when(verifyCredentialResponse.getUser()).thenReturn(null);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(loginRequest));
    }

    @Test
    void testAuthenticate_VerifyCredentialResponseNotSuccess() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        Mockito.when(loginRequest.getTenantId()).thenReturn("test-tenant");

        VerifyCredentialResponse verifyCredentialResponse = Mockito.mock(VerifyCredentialResponse.class);
        Mockito.when(credentialService.verifyCredentials(loginRequest)).thenReturn(verifyCredentialResponse);
        Mockito.when(verifyCredentialResponse.getStatus()).thenReturn(VerifyCredentialStatus.INVALID_CREDENTIAL);
        Mockito.when(verifyCredentialResponse.getUser()).thenReturn(Mockito.mock(UserResponse.class));

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.authenticate(loginRequest));
    }

    @Test
    void testRefresh() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("refresh-token");

        RefreshToken refreshToken = Mockito.mock(RefreshToken.class);
        Mockito.when(refreshToken.getUserId()).thenReturn("user-id");

        Mockito.when(tokenService.getResourceByRefreshToken("refresh-token")).thenReturn(refreshToken);

        UserResponse userResponse = Mockito.mock(UserResponse.class);
        Mockito.when(userResponse.getId()).thenReturn("user-id");
        Mockito.when(userResponse.isEnabled()).thenReturn(true);
        Mockito.when(userResponse.getTenantId()).thenReturn("tenant-id");

        Mockito.when(userService.getById("user-id")).thenReturn(userResponse);

        List<RoleWithPermissions> roleWithPermissions = new ArrayList<>();
        roleWithPermissions.add(Mockito.mock(RoleWithPermissions.class));
        Mockito.when(userService.getRoleWithPermissionsByUserId("user-id")).thenReturn(roleWithPermissions);

        long expiredAt = System.currentTimeMillis() + 10000;

        AccessToken accessToken = Mockito.mock(AccessToken.class);
        Mockito.when(accessToken.getAccessToken()).thenReturn("access-token");
        Mockito.when(accessToken.getExpiredAt()).thenReturn(expiredAt);
        Mockito.when(tokenService.generateAccessToken(Mockito.any())).thenReturn(accessToken);
        Mockito.when(tokenService.generateRefreshToken(Mockito.any())).thenReturn("refresh-token");

        LoginResponse result = authenticationService.refresh(request);

        Assertions.assertEquals(result.getAccessToken(), "access-token");
        Assertions.assertEquals(result.getUserId(), "user-id");
        Assertions.assertEquals(result.getTenantId(), "tenant-id");
        Assertions.assertEquals(result.getRefreshToken(), "refresh-token");
        Assertions.assertEquals(result.getExpiresAt(), expiredAt);
    }

    @Test
    void testRefresh_UserNull() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("refresh-token");

        RefreshToken refreshToken = Mockito.mock(RefreshToken.class);
        Mockito.when(refreshToken.getUserId()).thenReturn("user-id");

        Mockito.when(tokenService.getResourceByRefreshToken("refresh-token")).thenReturn(refreshToken);

        Mockito.when(userService.getById("user-id")).thenReturn(null);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.refresh(request));
    }

    @Test
    void testRefresh_UserNotEnabled() {
        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("refresh-token");

        RefreshToken refreshToken = Mockito.mock(RefreshToken.class);
        Mockito.when(refreshToken.getUserId()).thenReturn("user-id");

        Mockito.when(tokenService.getResourceByRefreshToken("refresh-token")).thenReturn(refreshToken);

        UserResponse userResponse = Mockito.mock(UserResponse.class);
        Mockito.when(userResponse.getId()).thenReturn("user-id");
        Mockito.when(userResponse.isEnabled()).thenReturn(false);
        Mockito.when(userService.getById("user-id")).thenReturn(userResponse);

        Assertions.assertThrows(AuthenticationException.class, () -> authenticationService.refresh(request));
    }

    @Test
    void testLogout() {
        LogoutRequest logoutRequest = Mockito.mock(LogoutRequest.class);
        Mockito.when(logoutRequest.getRefreshToken()).thenReturn("refresh-token");

        authenticationService.logout(logoutRequest);

        Mockito.verify(tokenService, Mockito.times(1)).expireRefreshToken("refresh-token");
    }

    @Test
    void testGetJwks() {
        String tenantId = "test-tenant";
        JwtKeyResponse jwtKeyResponse = Mockito.mock(JwtKeyResponse.class);
        KeyPair keyPair = CryptoUtil.generateKeyPair("RSA", 2048);

        Mockito.when(jwtKeyResponse.getPublicKey()).thenReturn(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        Mockito.when(jwtKeyResponse.getKeyAlgorithm()).thenReturn(KeyAlgorithm.ES256);
        Mockito.when(jwtKeyResponse.getId()).thenReturn("kid");

        Mockito.when(jwtKeyService.getTenantActiveJwtKey(tenantId)).thenReturn(jwtKeyResponse);
        Mockito.when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");

        List<Object> keys = authenticationService.getJwks(tenantId);

        Assertions.assertEquals(keys.size(), 1);
    }
}