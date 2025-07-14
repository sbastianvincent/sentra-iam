package com.svincent7.sentraiam.identity.service.role;

import com.svincent7.sentraiam.identity.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.identity.dto.role.ModifyPermissionRequest;
import com.svincent7.sentraiam.identity.dto.role.RoleRequest;
import com.svincent7.sentraiam.identity.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.RoleEntity;

import java.util.List;

public abstract class RoleService extends BaseService<RoleEntity, RoleRequest, RoleResponse, String> {
    public abstract RoleResponse getByRoleNameAndTenantId(String roleName, String tenantId);
    public abstract List<PermissionResponse> getPermissionsByRoleId(String roleId);
    public abstract List<PermissionResponse> addPermissionToRoleId(String roleId, ModifyPermissionRequest request);
    public abstract List<PermissionResponse> deletePermissionFromRoleId(String roleId, ModifyPermissionRequest request);
}
