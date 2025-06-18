package com.svincent7.sentraiam.identity.auth.endpoint;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.common.permission.Permission;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointRuleProviderImpl implements EndpointRuleProvider {

    @Override
    public List<EndpointRule> getEndpointRules() {
        return List.of(
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_LIST.getPermission()),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_GET.getPermission()),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants/by-name/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_GET.getPermission()),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/tenants",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_CREATE.getPermission()),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/tenants/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_UPDATE.getPermission()),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/users",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_LIST.getPermission()),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/users/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_GET.getPermission()),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/users",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_CREATE.getPermission()),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/users/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_UPDATE.getPermission()),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/users/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_DELETE.getPermission()),

            new EndpointRule(HttpMethod.POST, "/api/identity/v1/credentials",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_CREATE.getPermission()),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/credentials/*",
                        EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_DELETE.getPermission())
        );
    }

    @Override
    public String[] getPermittedEndpoints() {
        return new String[0];
    }
}
