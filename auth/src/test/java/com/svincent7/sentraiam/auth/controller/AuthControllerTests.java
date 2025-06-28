package com.svincent7.sentraiam.auth.controller;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import com.svincent7.sentraiam.auth.service.AuthenticationService;
import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.auth.LoginResponse;
import com.svincent7.sentraiam.common.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.common.dto.auth.RefreshRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class AuthControllerTests {
    private AuthenticationService authenticationService;
    private SentraIamAuthConfig authConfig;
    private AuthController authController;

    @BeforeEach
    void setup() {
        authenticationService = Mockito.mock(AuthenticationService.class);
        authConfig = Mockito.mock(SentraIamAuthConfig.class);
        authController = new AuthController(authenticationService, authConfig);
    }

    @Test
    void testLogin() {
        LoginRequest loginRequest = Mockito.mock(LoginRequest.class);
        LoginResponse loginResponse = Mockito.mock(LoginResponse.class);
        Mockito.when(authenticationService.authenticate(loginRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.login(loginRequest);

        Mockito.verify(authenticationService, Mockito.times(1)).authenticate(loginRequest);
        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertEquals(response.getBody(), loginResponse);
    }

    @Test
    void testRefresh() {
        RefreshRequest refreshRequest = Mockito.mock(RefreshRequest.class);
        LoginResponse loginResponse = Mockito.mock(LoginResponse.class);
        Mockito.when(authenticationService.refresh(refreshRequest)).thenReturn(loginResponse);

        ResponseEntity<LoginResponse> response = authController.refresh(refreshRequest);

        Mockito.verify(authenticationService, Mockito.times(1)).refresh(refreshRequest);
        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertEquals(response.getBody(), loginResponse);
    }

    @Test
    void testLogout() {
        LogoutRequest logoutRequest = Mockito.mock(LogoutRequest.class);
        ResponseEntity<Void> response = authController.logout(logoutRequest);

        Mockito.verify(authenticationService, Mockito.times(1)).logout(logoutRequest);
        Assertions.assertEquals(response.getStatusCodeValue(), 204);
        Assertions.assertNull(response.getBody());
    }

    @Test
    void testOpenIdConfiguration () {
        Mockito.when(authConfig.getOpenidConfigurationUri()).thenReturn("http://localhost:8080/auth/v1/keys/");

        String tenantId = "test-tenant";
        Map<String, Object> response = authController.openIdConfiguration(tenantId);

        Mockito.verify(authConfig, Mockito.times(2)).getOpenidConfigurationUri();
        Assertions.assertEquals(response.get("issuer"), "http://localhost:8080/auth/v1/keys/" + tenantId);
        Assertions.assertEquals(response.get("jwks_uri"), "http://localhost:8080/auth/v1/keys/" + tenantId + "/.well-known/jwks.json");
    }

    @Test
    void testGetJwks () {
        String tenantId = "test-tenant";
        Mockito.when(authenticationService.getJwks(tenantId)).thenReturn(List.of(Map.of("key", "value")));

        Map<String, Object> response = authController.getJwks(tenantId);
        Mockito.verify(authenticationService, Mockito.times(1)).getJwks(tenantId);
        Assertions.assertEquals(response.get("keys"), List.of(Map.of("key", "value")));
        Assertions.assertEquals(response.size(), 1);
        Assertions.assertTrue(response.containsKey("keys"));
        Assertions.assertTrue(response.get("keys") instanceof List);
        Assertions.assertEquals(((List<?>) response.get("keys")).size(), 1);
        Assertions.assertTrue(((List<?>) response.get("keys")).get(0) instanceof Map);
        Assertions.assertEquals(((Map<?, ?>) ((List<?>) response.get("keys")).get(0)).size(), 1);
        Assertions.assertTrue(((Map<?, ?>) ((List<?>) response.get("keys")).get(0)).containsKey("key"));
    }
}
