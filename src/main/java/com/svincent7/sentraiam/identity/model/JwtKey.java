package com.svincent7.sentraiam.identity.model;

import com.svincent7.sentraiam.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "jwt_keys")
public class JwtKey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "key_version", nullable = false)
    private String keyVersion;

    @Column(name = "private_key", nullable = false, columnDefinition = "TEXT")
    private String privateKey;

    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey;

    @Column(name = "key_algorithm", nullable = false)
    @Enumerated(EnumType.STRING)
    private KeyAlgorithm keyAlgorithm;

    @Column(name = "key_length", nullable = false)
    private int keyLength;

    @Column(name = "expired_timestamp", nullable = false)
    private long expiredTimestamp;

    public boolean isExpired() {
        return expiredTimestamp < System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "JwtKey{"
                + "id='" + id + '\''
                + ", tenantId='" + tenantId + '\''
                + ", keyVersion='" + keyVersion + '\''
                + ", privateKey=[PROTECTED]" + '\''
                + ", publicKey='" + publicKey + '\''
                + ", keyAlgorithm=" + keyAlgorithm
                + ", createdTimestamp=" + getCreatedTimestamp()
                + ", expiredTimestamp=" + expiredTimestamp
                + '}';
    }
}
