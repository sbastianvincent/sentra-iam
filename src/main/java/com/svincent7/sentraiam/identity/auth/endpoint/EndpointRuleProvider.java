package com.svincent7.sentraiam.identity.auth.endpoint;

import java.util.List;

public interface EndpointRuleProvider {
    String SCOPE_PREFIX = "SCOPE_";

    List<EndpointRule> getEndpointRules();
    String[] getPermittedEndpoints();
}
