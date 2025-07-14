package com.svincent7.sentraiam.identity.service.tenant;

import com.svincent7.sentraiam.identity.dto.tenant.TenantRequest;
import com.svincent7.sentraiam.identity.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.common.service.BaseService;
import com.svincent7.sentraiam.identity.model.TenantEntity;

public abstract class TenantService extends BaseService<TenantEntity, TenantRequest, TenantResponse, String> {
    public abstract TenantResponse getTenantByName(String tenantName);
    public abstract void saveTenant(TenantEntity tenantEntity);
}
