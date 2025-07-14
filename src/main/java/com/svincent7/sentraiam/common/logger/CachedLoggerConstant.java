package com.svincent7.sentraiam.common.logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class CachedLoggerConstant {

    private CachedLoggerConstant() {
        throw new UnsupportedOperationException();
    }
    public static final String REQUEST_PREFIX = "[REQUEST] : ";
    public static final String RESPONSE_PREFIX = "[RESPONSE]: ";

    public static final Set<String> EXCLUDED_RESPONSE_PATHS = new HashSet<>(
            Arrays.asList(
                    "/actuator/prometheus",
                    "/actuator/metrics"
            ));
}
