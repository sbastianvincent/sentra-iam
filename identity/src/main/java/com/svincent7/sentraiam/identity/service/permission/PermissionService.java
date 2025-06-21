package com.svincent7.sentraiam.identity.service.permission;

import com.svincent7.sentraiam.common.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.identity.model.PermissionEntity;

import java.util.List;

public interface PermissionService {
    PermissionEntity getPermissionByName(String permission);
    PermissionEntity getPermissionById(String permissionId);
    PermissionEntity addPermission(String permission, boolean userVisible);
    List<PermissionResponse> getPermissions();
    List<PermissionResponse> getPermissionsUserVisible();
}
