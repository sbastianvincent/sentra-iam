package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.identity.dto.auth.LoginRequest;
import com.svincent7.sentraiam.identity.dto.auth.LoginResponse;
import com.svincent7.sentraiam.identity.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.identity.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Slf4j
public class AuthController {
    private final AuthenticationService authenticationService;
    private final IdentityConfig config;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid final @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.authenticate(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Valid final @RequestBody RefreshRequest request) {
        LoginResponse login = authenticationService.refresh(request);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid final @RequestBody LogoutRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/keys/{tenantId}/.well-known/openid-configuration")
    public Map<String, Object> openIdConfiguration(final @PathVariable String tenantId) {
        return Map.of(
                "issuer", config.getOpenidConfigurationUri() + tenantId,
                "jwks_uri", config.getOpenidConfigurationUri() + tenantId + "/.well-known/jwks.json"
        );
    }

    @GetMapping(path = "/keys/{tenantId}/.well-known/jwks.json")
    public Map<String, Object> getJwks(final @PathVariable String tenantId) {
        return Map.of("keys", authenticationService.getJwks(tenantId));
    }
}
