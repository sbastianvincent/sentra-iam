package com.svincent7.sentraiam.identity.service.role;

import com.svincent7.sentraiam.common.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.common.dto.role.ModifyPermissionRequest;
import com.svincent7.sentraiam.common.dto.role.RoleRequest;
import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.PermissionEntity;
import com.svincent7.sentraiam.identity.model.RoleEntity;
import com.svincent7.sentraiam.identity.model.RolePermissionMapping;
import com.svincent7.sentraiam.identity.repository.RolePermissionRepository;
import com.svincent7.sentraiam.identity.repository.RoleRepository;
import com.svincent7.sentraiam.identity.service.permission.PermissionMapper;
import com.svincent7.sentraiam.identity.service.permission.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;
    private final PermissionMapper permissionMapper;

    @Override
    protected JpaRepository<RoleEntity, String> getRepository() {
        return roleRepository;
    }

    @Override
    protected BaseMapper<RoleEntity, RoleRequest, RoleResponse> getMapper() {
        return roleMapper;
    }

    @Override
    public RoleResponse getByRoleNameAndTenantId(final String roleName, final String tenantId) {
        RoleEntity role = roleRepository.getByRoleNameAndTenantId(roleName, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found: " + roleName
                        + " with tenantId: " + tenantId));
        return getMapper().toResponseDTO(role);
    }

    @Override
    public List<PermissionResponse> getPermissionsByRoleId(final String roleId) {
        List<RolePermissionMapping> rolePermissionMappings = rolePermissionRepository.findByRoleId(roleId);
        return rolePermissionMappings.stream().map(permissionMapping ->
            permissionMapper.toResponseDTO(permissionMapping.getPermission())
        ).toList();
    }

    @Override
    public List<PermissionResponse> addPermissionToRoleId(final String roleId, final ModifyPermissionRequest request) {
        PermissionEntity permission = permissionService.getPermissionById(request.getPermissionId());
        RoleEntity role = getResourceById(roleId);
        RolePermissionMapping mapping = new RolePermissionMapping(role, permission);
        rolePermissionRepository.save(mapping);
        return getPermissionsByRoleId(roleId);
    }

    @Override
    public List<PermissionResponse> deletePermissionFromRoleId(final String roleId,
                                                               final ModifyPermissionRequest request) {
        PermissionEntity permission = permissionService.getPermissionById(request.getPermissionId());
        RoleEntity role = getResourceById(roleId);
        RolePermissionMapping mapping = new RolePermissionMapping(role, permission);
        rolePermissionRepository.delete(mapping);
        return getPermissionsByRoleId(roleId);
    }
}
