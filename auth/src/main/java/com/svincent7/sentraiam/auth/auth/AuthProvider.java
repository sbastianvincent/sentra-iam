package com.svincent7.sentraiam.auth.auth;

import com.svincent7.sentraiam.common.auth.token.AuthTokenProvider;
import com.svincent7.sentraiam.common.auth.token.DefaultAuthTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider {

    @Bean
    public AuthTokenProvider getAuthTokenProvider() {
        return new DefaultAuthTokenProvider();
    }
}
