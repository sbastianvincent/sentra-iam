package com.svincent7.sentraiam.identity.service.role;

import com.svincent7.sentraiam.common.dto.role.RoleRequest;
import com.svincent7.sentraiam.common.dto.role.RoleResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.model.RoleEntity;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import lombok.Getter;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Getter
@Mapper(componentModel = "spring")
public abstract class RoleMapper implements BaseMapper<RoleEntity, RoleRequest, RoleResponse> {
    private TenantService tenantService;

    @Autowired
    public void setTenantService(final TenantService injectedTenantService) {
        this.tenantService = injectedTenantService;
    }

    @Override
    public void updateEntityFromDTO(final RoleRequest request, final RoleEntity entity) {
        entity.setRoleName(request.getRoleName());
    }

    @Override
    public RoleEntity toEntity(final RoleRequest request) {
        if (request == null) {
            return null;
        }
        TenantEntity tenant = tenantService.getResourceById(request.getTenantId());

        RoleEntity role = new RoleEntity();
        role.setId(UUID.randomUUID().toString());
        role.setTenant(tenant);
        role.setTenantId(request.getTenantId());
        updateEntityFromDTO(request, role);

        return role;
    }
}
