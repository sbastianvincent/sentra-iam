package com.svincent7.sentraiam.identity.service.user;

import com.svincent7.sentraiam.identity.dto.role.RoleResponse;
import com.svincent7.sentraiam.identity.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.identity.dto.user.ModifyRoleRequest;
import com.svincent7.sentraiam.identity.dto.user.UserRequest;
import com.svincent7.sentraiam.identity.dto.user.UserResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.UserEntity;

import java.util.List;

public abstract class UserService extends BaseService<UserEntity, UserRequest, UserResponse, String> {
    public abstract UserResponse getByUsernameAndTenantId(String username, String tenantId);
    public abstract List<RoleResponse> getRoleByUserId(String userId);
    public abstract List<RoleResponse> addRoleToUserId(String userId, ModifyRoleRequest modifyRoleRequest);
    public abstract List<RoleResponse> deleteRoleFromUserId(String userId, ModifyRoleRequest modifyRoleRequest);
    public abstract List<RoleWithPermissions> getRoleWithPermissionsByUserId(String userId);
}
