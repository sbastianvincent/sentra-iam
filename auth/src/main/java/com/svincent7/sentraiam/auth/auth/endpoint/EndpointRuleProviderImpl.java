package com.svincent7.sentraiam.auth.auth.endpoint;

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
            new EndpointRule(HttpMethod.POST, "/api/auth/v1/introspect",
                        Set.of(
                                EndpointRuleProvider.SCOPE_PREFIX + Permission.TOKEN_INTROSPECT.getPermission()
                        ))
        );
    }

    @Override
    public String[] getPermittedEndpoints() {
        return new String[]{
            "/api/auth/v1/login",
            "/api/auth/v1/refresh",
            "/api/auth/v1/logout",
        };
    }
}
