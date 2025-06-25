package com.svincent7.sentraiam.common.client;

import com.svincent7.sentraiam.common.dto.tenant.TenantResponse;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface IdentityClient {

    @RequestMapping(value = "/api/identity/v1/tenants/{tenantId}", method = RequestMethod.GET)
    ResponseEntity<TenantResponse> getTenantById(@PathVariable String tenantId);

    @RequestMapping(value = "/api/identity/v1/tenants/by-name/{tenantName}", method = RequestMethod.GET)
    ResponseEntity<TenantResponse> getTenantByName(@PathVariable String tenantName);

    @RequestMapping(value = "/api/identity/v1/users/{userId}", method = RequestMethod.GET)
    ResponseEntity<UserResponse> getUser(@PathVariable String userId);
}
