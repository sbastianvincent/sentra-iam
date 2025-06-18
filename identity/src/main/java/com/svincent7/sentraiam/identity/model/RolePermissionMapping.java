package com.svincent7.sentraiam.identity.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Data
@Entity
@Table(name = "role_permissions")
@NoArgsConstructor
public class RolePermissionMapping implements Serializable {

    @EmbeddedId
    private RolePermissionKey rolePermissionKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RoleEntity role;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private PermissionEntity permission;

    public RolePermissionMapping(final RoleEntity roleInput, final PermissionEntity permissionInput) {
        this.role = roleInput;
        this.permission = permissionInput;
        this.rolePermissionKey = new RolePermissionKey(role.getId(), permission.getId());
    }
}
