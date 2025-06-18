package com.svincent7.sentraiam.identity.controller;

import com.svincent7.sentraiam.common.controller.BaseController;
import com.svincent7.sentraiam.common.dto.tenant.TenantRequest;
import com.svincent7.sentraiam.common.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.identity.model.TenantEntity;
import com.svincent7.sentraiam.identity.service.tenant.TenantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity/v1/tenants")
@Slf4j
public class TenantController extends BaseController<TenantEntity, TenantRequest, TenantResponse, String> {
    private final TenantService tenantService;

    @Override
    protected TenantService getService() {
        return tenantService;
    }

    @RequestMapping(value = "/by-name/{tenantName}", method = RequestMethod.GET)
    public ResponseEntity<TenantResponse> getTenantByName(final @PathVariable String tenantName) {
        TenantResponse tenantResponse = tenantService.getTenantByName(tenantName);
        return ResponseEntity.ok(tenantResponse);
    }
}
