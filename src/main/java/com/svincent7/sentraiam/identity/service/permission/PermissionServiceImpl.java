package com.svincent7.sentraiam.identity.service.permission;

import com.svincent7.sentraiam.identity.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.identity.model.PermissionEntity;
import com.svincent7.sentraiam.identity.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public PermissionEntity getPermissionByName(final String permission) {
        return permissionRepository.findByPermission(permission).orElseThrow(
                () -> new ResourceNotFoundException("Permission " + permission + " not found"));
    }

    @Override
    public PermissionEntity getPermissionById(final String permissionId) {
        return permissionRepository.findById(permissionId).orElseThrow(
                () -> new ResourceNotFoundException("Permission with id " + permissionId + " not found"));
    }

    @Override
    public PermissionEntity addPermission(final String permission, final boolean userVisible) {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setId(UUID.randomUUID().toString());
        permissionEntity.setPermission(permission);
        permissionEntity.setUserVisible(userVisible);
        return permissionRepository.save(permissionEntity);
    }

    @Override
    public List<PermissionResponse> getPermissions() {
        List<PermissionEntity> permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toResponseDTO).toList();
    }

    @Override
    public List<PermissionResponse> getPermissionsUserVisible() {
        List<PermissionEntity> permissions = permissionRepository.findByUserVisible(true);
        return permissions.stream().map(permissionMapper::toResponseDTO).toList();
    }
}
