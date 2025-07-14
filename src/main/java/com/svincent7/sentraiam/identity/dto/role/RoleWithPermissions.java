package com.svincent7.sentraiam.identity.dto.role;

import com.svincent7.sentraiam.identity.dto.permission.PermissionResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class RoleWithPermissions extends RoleResponse {
    private List<PermissionResponse> permissions;
}
