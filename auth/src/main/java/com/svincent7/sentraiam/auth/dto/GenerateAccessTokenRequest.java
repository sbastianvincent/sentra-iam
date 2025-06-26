package com.svincent7.sentraiam.auth.dto;

import com.svincent7.sentraiam.common.dto.role.RoleWithPermissions;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class GenerateAccessTokenRequest {
    private UserResponse user;
    private List<RoleWithPermissions> roles;

    public Set<String> getPermissions() {
        Set<String> permissions = new HashSet<>();
        for (RoleWithPermissions role : roles) {
            role.getPermissions().forEach(permission -> permissions.add(permission.getPermission()));
        }
        return permissions;
    }
}
