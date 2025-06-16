package com.svincent7.sentraiam.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "created_timestamp", nullable = false)
    private long createdTimestamp;

    @Column(name = "expired_timestamp", nullable = false)
    private long expiredTimestamp;

    public boolean isExpired() {
        return System.currentTimeMillis() > expiredTimestamp;
    }
}
