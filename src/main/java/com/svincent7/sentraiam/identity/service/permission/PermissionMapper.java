package com.svincent7.sentraiam.identity.service.permission;

import com.svincent7.sentraiam.identity.dto.permission.PermissionResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.PermissionEntity;
import lombok.Getter;
import org.mapstruct.Mapper;

import java.util.UUID;

@Getter
@Mapper(componentModel = "spring")
public abstract class PermissionMapper implements BaseMapper<PermissionEntity, PermissionResponse, PermissionResponse> {
    @Override
    public void updateEntityFromDTO(final PermissionResponse request, final PermissionEntity entity) {
    }

    @Override
    public PermissionEntity toEntity(final PermissionResponse request) {
        if (request == null) {
            return null;
        }

        PermissionEntity permission = new PermissionEntity();
        permission.setId(UUID.randomUUID().toString());
        permission.setPermission(request.getPermission());
        permission.setUserVisible(request.isUserVisible());
        updateEntityFromDTO(request, permission);

        return permission;
    }
}
