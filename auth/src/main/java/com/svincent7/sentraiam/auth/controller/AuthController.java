package com.svincent7.sentraiam.auth.controller;

import com.svincent7.sentraiam.auth.service.AuthenticationService;
import com.svincent7.sentraiam.auth.service.token.TokenService;
import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.auth.LoginResponse;
import com.svincent7.sentraiam.common.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.common.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/v1")
@Slf4j
public class AuthController {
    private static final String TOKEN = "token";
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

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

    @PostMapping(path = "/introspect", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity<Map<String, Object>> introspect(final @RequestParam MultiValueMap<String, String> body) {
        String token = body.getFirst(TOKEN);
        return ResponseEntity.ok(getToken(token));
    }

    private Map<String, Object> getToken(final String token) {
        Map<String, Object> response = new HashMap<>();
        if (StringUtils.isEmpty(token)) {
            log.warn("Validate Token error: Token is empty");
            response.put("active", false);
            return response;
        }

        try {
            UserResponse authentication = tokenService.authenticate(token);
            response.put("active", true);
            response.put("sub", authentication.getId());
            response.put("username", authentication.getUsername());
            response.put("exp", authentication.getExpiration());
            response.put("iss", authentication.getTenantId());
        } catch (Exception e) {
            log.warn("Validate Token error: {}", e.getMessage());
            response.put("active", false);
        }
        return response;
    }
}
