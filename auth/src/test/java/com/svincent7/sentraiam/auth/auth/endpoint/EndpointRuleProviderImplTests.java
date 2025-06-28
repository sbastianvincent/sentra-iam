package com.svincent7.sentraiam.auth.auth.endpoint;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EndpointRuleProviderImplTests {

    @Test
    void testGetEndpointRules() {
        EndpointRuleProviderImpl endpointRuleProvider = new EndpointRuleProviderImpl();
        Assertions.assertNotNull(endpointRuleProvider.getEndpointRules());
        Assertions.assertEquals(endpointRuleProvider.getEndpointRules().size(), 0 );
    }

    @Test
    void testGetPermittedEndpoints() {
        EndpointRuleProviderImpl endpointRuleProvider = new EndpointRuleProviderImpl();
        Assertions.assertNotNull(endpointRuleProvider.getPermittedEndpoints());
        Assertions.assertEquals(endpointRuleProvider.getPermittedEndpoints().length, 4);
    }
}
