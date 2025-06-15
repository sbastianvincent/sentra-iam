package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.common.dto.tenant.TenantRequest;
import com.svincent7.sentraiam.common.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/tenants")
public class TenantController extends BaseController<TenantEntity, TenantRequest, TenantResponse, String> {
    private final TenantService tenantService;

    @Override
    protected TenantService getService() {
        return tenantService;
    }
}
