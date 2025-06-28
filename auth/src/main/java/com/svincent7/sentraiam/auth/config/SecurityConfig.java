package com.svincent7.sentraiam.auth.config;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.common.cert.SSLBundleEurekaClientHttpRequestFactorySupplier;
import com.svincent7.sentraiam.common.config.ConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final EndpointRuleProvider endpointRuleProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(endpointRuleProvider.getPermittedEndpoints()).permitAll();
                    auth.anyRequest().denyAll();
                });

        return httpSecurity.build();
    }

    @Bean
    public EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier(final ConfigProperties config,
                                                                                         final SslBundles sslBundles) {
        return new SSLBundleEurekaClientHttpRequestFactorySupplier(config, sslBundles);
    }

}
