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
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"role_name", "tenant_id"})})
public class RoleEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "role_name", nullable = false, unique = true, columnDefinition = "varchar(128)")
    private String roleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private TenantEntity tenant;

    @Column(name = "tenant_id", insertable = false, updatable = false, columnDefinition = "varchar(36)")
    private String tenantId;
}
