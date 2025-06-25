package com.svincent7.sentraiam.auth.auth.endpoint;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EndpointRuleProviderImpl implements EndpointRuleProvider {

    @Override
    public List<EndpointRule> getEndpointRules() {
        return List.of();
    }

    @Override
    public String[] getPermittedEndpoints() {
        return new String[]{
            "/api/auth/v1/login",
            "/api/auth/v1/refresh",
            "/api/auth/v1/logout",
            "/api/auth/v1/keys/**"
        };
    }
}
