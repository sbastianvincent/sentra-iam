package com.svincent7.sentraiam.identity.model;

import lombok.Data;

@Data
public class AccessToken {
    private String tenantId;
    private String userId;
    private String accessToken;
    private long createdAt;
    private long expiredAt;
}
