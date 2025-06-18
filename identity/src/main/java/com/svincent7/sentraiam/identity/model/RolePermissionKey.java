package com.svincent7.sentraiam.identity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class RolePermissionKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1198273981723998L;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "permission_id")
    private String permissionId;

    public RolePermissionKey(final String role, final String permission) {
        this.roleId = role;
        this.permissionId = permission;
    }
}
