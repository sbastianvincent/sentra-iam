package com.svincent7.sentraiam.identity.model;

import com.svincent7.sentraiam.common.dto.credential.CredentialType;
import com.svincent7.sentraiam.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "credentials", uniqueConstraints = {@UniqueConstraint(columnNames = {"identifier", "type"})})
public class CredentialEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "type", nullable = false, columnDefinition = "varchar(32)")
    @Enumerated(EnumType.STRING)
    private CredentialType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "user_id", insertable = false, updatable = false, columnDefinition = "varchar(36)")
    private String userId;

    @Column(name = "identifier", unique = true, nullable = false, columnDefinition = "varchar(255)")
    private String identifier;

    @Column(name = "secret_data", nullable = false, columnDefinition = "TEXT")
    private String secretData;

    @Column(name = "credential_data", nullable = false, columnDefinition = "TEXT")
    private String credentialData;

    @Column(name = "revoked", nullable = false, columnDefinition = "boolean default false")
    private boolean revoked;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private int version;

    @Override
    public void entityUpdate() {
        version++;
    }
}
