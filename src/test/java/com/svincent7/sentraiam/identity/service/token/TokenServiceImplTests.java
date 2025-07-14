package com.svincent7.sentraiam.identity.service.token;

import com.svincent7.sentraiam.common.exception.BadRequestException;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.dto.credential.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.identity.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.identity.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.identity.dto.user.UserResponse;
import com.svincent7.sentraiam.identity.model.AccessToken;
import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import com.svincent7.sentraiam.identity.model.RefreshToken;
import com.svincent7.sentraiam.identity.repository.RefreshTokenRepository;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyResponse;
import com.svincent7.sentraiam.identity.service.jwtkey.JwtKeyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TokenServiceImplTests {

    @Mock
    private IdentityConfig config;
    @Mock
    private TokenGenerator tokenGenerator;
    @Mock
    private JwtKeyService jwtKeyService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private final String userId = "user-123";
    private final String tenantId = "tenant-abc";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateAccessToken_success() throws Exception {
        GenerateAccessTokenRequest request = new GenerateAccessTokenRequest();
        UserResponse user = new UserResponse();
        user.setId(userId);
        user.setTenantId(tenantId);
        user.setUsername("johndoe");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setVersion(1);

        request.setUser(user);
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setPermission("permission");

        RoleWithPermissions role = new RoleWithPermissions();
        role.setPermissions(List.of(permissionResponse));

        List<RoleWithPermissions> roles = new ArrayList<>();
        roles.add(role);

        request.setRoles(roles);

        // Generate test key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        JwtKeyResponse jwtKeyResponse = new JwtKeyResponse();
        jwtKeyResponse.setId("jwt-key-id");
        jwtKeyResponse.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        jwtKeyResponse.setKeyAlgorithm(KeyAlgorithm.RS256);

        when(config.getTokenExpirationMinutes()).thenReturn(60);
        when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");
        when(config.getOpenidConfigurationUri()).thenReturn("https://example.com/");
        when(jwtKeyService.getTenantActiveJwtKey(tenantId)).thenReturn(jwtKeyResponse);
        when(tokenGenerator.generateToken(any(CreateTokenRequest.class))).thenReturn("mocked.jwt.token");

        AccessToken accessToken = tokenService.generateAccessToken(request);

        assertNotNull(accessToken);
        assertEquals("mocked.jwt.token", accessToken.getAccessToken());
        assertEquals(userId, accessToken.getUserId());
        assertEquals(tenantId, accessToken.getTenantId());
    }

    @Test
    void testGenerateAccessToken_withoutFirstnameLastName() throws Exception {
        GenerateAccessTokenRequest request = new GenerateAccessTokenRequest();
        UserResponse user = new UserResponse();
        user.setId(userId);
        user.setTenantId(tenantId);
        user.setUsername("johndoe");
        user.setVersion(1);

        request.setUser(user);
        PermissionResponse permissionResponse = new PermissionResponse();
        permissionResponse.setPermission("permission");

        RoleWithPermissions role = new RoleWithPermissions();
        role.setPermissions(List.of(permissionResponse));

        List<RoleWithPermissions> roles = new ArrayList<>();
        roles.add(role);

        request.setRoles(roles);

        // Generate test key pair
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        JwtKeyResponse jwtKeyResponse = new JwtKeyResponse();
        jwtKeyResponse.setId("jwt-key-id");
        jwtKeyResponse.setPrivateKey(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
        jwtKeyResponse.setKeyAlgorithm(KeyAlgorithm.RS256);

        when(config.getTokenExpirationMinutes()).thenReturn(60);
        when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");
        when(config.getOpenidConfigurationUri()).thenReturn("https://example.com/");
        when(jwtKeyService.getTenantActiveJwtKey(tenantId)).thenReturn(jwtKeyResponse);
        when(tokenGenerator.generateToken(any(CreateTokenRequest.class))).thenReturn("mocked.jwt.token");

        AccessToken accessToken = tokenService.generateAccessToken(request);

        assertNotNull(accessToken);
        assertEquals("mocked.jwt.token", accessToken.getAccessToken());
        assertEquals(userId, accessToken.getUserId());
        assertEquals(tenantId, accessToken.getTenantId());
    }

    @Test
    void testGenerateRefreshToken_success() {
        UserResponse user = new UserResponse();
        user.setId(userId);
        user.setTenantId(tenantId);

        RefreshToken mockToken = new RefreshToken();
        mockToken.setRefreshToken("mocked-refresh-token");

        when(config.getRefreshTokenExpirationDays()).thenReturn(7);
        when(refreshTokenRepository.save(any())).thenReturn(mockToken);

        String refreshToken = tokenService.generateRefreshToken(user);
        assertEquals("mocked-refresh-token", refreshToken);
    }

    @Test
    void testGetResourceByRefreshToken_success() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredTimestamp(System.currentTimeMillis() + 10000); // not expired

        when(refreshTokenRepository.findById("valid-token")).thenReturn(Optional.of(refreshToken));

        RefreshToken result = tokenService.getResourceByRefreshToken("valid-token");
        assertNotNull(result);
    }

    @Test
    void testGetResourceByRefreshToken_notFound() {
        when(refreshTokenRepository.findById("invalid-token")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                tokenService.getResourceByRefreshToken("invalid-token"));
    }

    @Test
    void testGetResourceByRefreshToken_expired() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredTimestamp(System.currentTimeMillis() - 10000); // expired

        when(refreshTokenRepository.findById("expired-token")).thenReturn(Optional.of(refreshToken));

        assertThrows(BadRequestException.class, () ->
                tokenService.getResourceByRefreshToken("expired-token"));
    }

    @Test
    void testExpireRefreshToken_success() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredTimestamp(System.currentTimeMillis() + 10000); // initially valid

        when(refreshTokenRepository.findById("valid-token")).thenReturn(Optional.of(refreshToken));
        when(refreshTokenRepository.save(any())).thenReturn(refreshToken);

        tokenService.expireRefreshToken("valid-token");

        assertTrue(refreshToken.getExpiredTimestamp() <= System.currentTimeMillis());
        verify(refreshTokenRepository).save(refreshToken);
    }
}
