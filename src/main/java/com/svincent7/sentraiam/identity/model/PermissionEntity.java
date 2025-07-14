package com.svincent7.sentraiam.identity.model;

import com.svincent7.sentraiam.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "permissions")
public class PermissionEntity extends BaseEntity {

    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "permission", nullable = false, unique = true, columnDefinition = "varchar(64)")
    private String permission;

    @Column(name = "user_visible", nullable = false)
    private boolean userVisible;
}
