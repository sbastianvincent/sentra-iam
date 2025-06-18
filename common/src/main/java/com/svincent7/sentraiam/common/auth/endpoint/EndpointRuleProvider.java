package com.svincent7.sentraiam.common.auth.endpoint;

import java.util.List;

public interface EndpointRuleProvider {
    String SCOPE_PREFIX = "SCOPE_";

    List<EndpointRule> getEndpointRules();
    String[] getPermittedEndpoints();
}
