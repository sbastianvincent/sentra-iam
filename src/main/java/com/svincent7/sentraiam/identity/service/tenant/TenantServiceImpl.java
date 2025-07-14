package com.svincent7.sentraiam.identity.service.tenant;

import com.svincent7.sentraiam.identity.dto.tenant.TenantRequest;
import com.svincent7.sentraiam.identity.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.service.BaseMapper;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl extends TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    protected JpaRepository<TenantEntity, String> getRepository() {
        return tenantRepository;
    }

    @Override
    protected BaseMapper<TenantEntity, TenantRequest, TenantResponse> getMapper() {
        return tenantMapper;
    }

    @Override
    public TenantResponse getTenantByName(final String tenantName) {
        TenantEntity tenant = tenantRepository.findByTenantName(tenantName)
                .orElseThrow(() -> new ResourceNotFoundException("Resource Not Found: " + tenantName));
        return getMapper().toResponseDTO(tenant);
    }

    @Override
    public void saveTenant(final TenantEntity tenantEntity) {
        getRepository().save(tenantEntity);
    }
}
