package com.svincent7.sentraiam.identity.service.permission;

import com.svincent7.sentraiam.identity.model.PermissionEntity;

public interface PermissionService {
    PermissionEntity getPermissionByName(String permission);
    PermissionEntity getPermissionById(String permissionId);
    PermissionEntity addPermission(String permission, boolean userVisible);
}
