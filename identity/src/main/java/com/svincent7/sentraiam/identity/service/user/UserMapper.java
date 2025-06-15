package com.svincent7.sentraiam.identity.service.user;

import com.svincent7.sentraiam.common.dto.user.UserRequest;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.model.UserEntity;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Getter
@Mapper(componentModel = "spring")
public abstract class UserMapper implements BaseMapper<UserEntity, UserRequest, UserResponse> {
    private TenantService tenantService;

    @Autowired
    public void setTenantService(final TenantService injectedTenantService) {
        this.tenantService = injectedTenantService;
    }

    @Override
    public void updateEntityFromDTO(final UserRequest request, final UserEntity entity) {
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setEnabled(request.isEnabled());
    }

    @Override
    public UserEntity toEntity(final UserRequest request) {
        if (request == null) {
            return null;
        }
        TenantEntity tenant = tenantService.getResourceById(request.getTenantId());

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(request.getUsername());
        user.setTenant(tenant);
        user.setTenantId(request.getTenantId());
        user.setVersion(1);
        updateEntityFromDTO(request, user);

        return user;
    }
}
