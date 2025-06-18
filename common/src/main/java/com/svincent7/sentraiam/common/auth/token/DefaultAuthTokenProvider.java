package com.svincent7.sentraiam.common.auth.token;

public class DefaultAuthTokenProvider implements AuthTokenProvider {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TEMP_SERVICE_ACCOUNT_AUTH_TOKEN = "TEMP_SERVICE_ACCOUNT_TOKEN";

    @Override
    public String getProviderAuthToken() {
        return BEARER_PREFIX + TEMP_SERVICE_ACCOUNT_AUTH_TOKEN;
    }
}
