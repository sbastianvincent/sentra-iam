package com.svincent7.sentraiam.identity.model;

import com.svincent7.sentraiam.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"username", "tenant_id"})})
public class UserEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "username", nullable = false, columnDefinition = "varchar(128)")
    private String username;

    @Column(name = "first_name", columnDefinition = "varchar(255)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(255)")
    private String lastName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false, columnDefinition = "varchar(36)")
    private String tenantId;
}
