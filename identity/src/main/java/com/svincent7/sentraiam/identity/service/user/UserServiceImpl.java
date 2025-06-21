package com.svincent7.sentraiam.identity.service.user;

import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.dto.user.ModifyRoleRequest;
import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.RoleEntity;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.model.UserRoleMapping;
import com.svincent7.sentraiam.identity.repository.UserRepository;
import com.svincent7.sentraiam.identity.repository.UserRoleRepository;
import com.svincent7.sentraiam.identity.service.role.RoleMapper;
import com.svincent7.sentraiam.identity.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @Override
    protected JpaRepository<UserEntity, String> getRepository() {
        return userRepository;
    }

    @Override
    protected BaseMapper<UserEntity, UserRequest, UserResponse> getMapper() {
        return userMapper;
    }

    @Override
    public UserResponse getByUsernameAndTenantId(final String username, final String tenantId) {
        UserEntity user = userRepository.getByUsernameAndTenantId(username, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found: " + username
                        + " with tenantId: " + tenantId));
        return getMapper().toResponseDTO(user);
    }

    @Override
    public List<RoleResponse> getRoleByUserId(final String userId) {
        List<UserRoleMapping> userRoleMappings = userRoleRepository.findByUserId(userId);
        return userRoleMappings.stream().map(roleMapping ->
                roleMapper.toResponseDTO(roleMapping.getRole())
        ).toList();
    }

    @Override
    public List<RoleResponse> addRoleToUserId(final String userId, final ModifyRoleRequest modifyRoleRequest) {
        UserEntity user = getResourceById(userId);
        RoleEntity roleEntity = roleService.getResourceById(modifyRoleRequest.getRoleId());
        UserRoleMapping mapping = new UserRoleMapping(user, roleEntity);
        userRoleRepository.save(mapping);
        return getRoleByUserId(userId);
    }

    @Override
    public List<RoleResponse> deleteRoleFromUserId(final String userId, final ModifyRoleRequest modifyRoleRequest) {
        UserEntity user = getResourceById(userId);
        RoleEntity roleEntity = roleService.getResourceById(modifyRoleRequest.getRoleId());
        UserRoleMapping mapping = new UserRoleMapping(user, roleEntity);
        userRoleRepository.delete(mapping);
        return getRoleByUserId(userId);
    }
}
