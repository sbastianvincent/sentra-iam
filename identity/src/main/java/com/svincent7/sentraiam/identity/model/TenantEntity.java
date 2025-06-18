package com.svincent7.sentraiam.identity.model;

import com.svincent7.sentraiam.common.dto.tenant.TenantStatus;
import com.svincent7.sentraiam.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tenants")
public class TenantEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "tenant_name", nullable = false, unique = true, columnDefinition = "varchar(128)")
    private String tenantName;

    @Column(nullable = false, columnDefinition = "varchar(16) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    private TenantStatus tenantStatus;
}
