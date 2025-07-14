package com.svincent7.sentraiam.identity.dto.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String userId;
    private String tenantId;
    private Long expiresAt;
}
