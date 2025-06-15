package com.svincent7.sentraiam.identity.service.tenant;

import com.svincent7.sentraiam.common.dto.tenant.TenantRequest;
import com.svincent7.sentraiam.common.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import lombok.Getter;
import org.mapstruct.Mapper;

import java.util.UUID;

@Getter
@Mapper(componentModel = "spring")
public abstract class TenantMapper implements BaseMapper<TenantEntity, TenantRequest, TenantResponse> {

    @Override
    public void updateEntityFromDTO(final TenantRequest request, final TenantEntity entity) {
        entity.setTenantStatus(request.getTenantStatus());
    }

    @Override
    public TenantEntity toEntity(final TenantRequest request) {
        if (request == null) {
            return null;
        }

        TenantEntity tenant = new TenantEntity();
        tenant.setId(UUID.randomUUID().toString());
        tenant.setTenantName(request.getTenantName());
        tenant.setVersion(1);
        updateEntityFromDTO(request, tenant);

        return tenant;
    }
}
