package com.svincent7.sentraiam.identity.auth.endpoint;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.common.permission.Permission;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class EndpointRuleProviderImpl implements EndpointRuleProvider {

    @Override
    public List<EndpointRule> getEndpointRules() {
        return List.of(
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/permissions/get-all",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.PERMISSIONS_GET_ALL.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/permissions",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.PERMISSIONS_GET_ALL.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.PERMISSIONS_LIST.getPermission()
                    )),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/roles",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_LIST.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/roles/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_GET.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/roles",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_CREATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/roles/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/roles/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_DELETE.getPermission()
                    )),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/roles/*/permissions",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_GET.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/roles/*/permissions",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_CREATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/roles/*/permissions",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.ROLES_DELETE.getPermission()
                    )),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_LIST.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_GET.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_UPDATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/tenants/by-name/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_GET.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/tenants",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_CREATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/tenants/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.TENANTS_UPDATE.getPermission()
                    )),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/users",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_LIST.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/users/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_GET.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/users",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_CREATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/users/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/users/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_DELETE.getPermission()
                    )),

            new EndpointRule(HttpMethod.GET, "/api/identity/v1/users/*/roles",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_LIST.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_GET.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/users/*/roles",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_UPDATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/users/*/roles",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_DELETE.getPermission()
                    )),

            new EndpointRule(HttpMethod.POST, "/api/identity/v1/credentials/verify",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_VERIFY.getPermission()
                    )),
            new EndpointRule(HttpMethod.GET, "/api/identity/v1/credentials/**",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_GET.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_CREATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_UPDATE.getPermission(),
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_DELETE.getPermission()
                    )),
            new EndpointRule(HttpMethod.POST, "/api/identity/v1/credentials",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_CREATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.PUT, "/api/identity/v1/credentials/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_UPDATE.getPermission()
                    )),
            new EndpointRule(HttpMethod.DELETE, "/api/identity/v1/credentials/*",
                    Set.of(
                            EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_DELETE.getPermission()
                    ))
        );
    }

    @Override
    public String[] getPermittedEndpoints() {
        return new String[0];
    }
}
