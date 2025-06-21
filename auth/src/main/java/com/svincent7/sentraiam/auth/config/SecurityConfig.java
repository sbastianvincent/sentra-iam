package com.svincent7.sentraiam.auth.config;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.common.permission.Permission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                    for (EndpointRule rule : endpointRuleProvider.getEndpointRules()) {
                        auth.requestMatchers(rule.method(), rule.path()).hasAnyRole(
                                rule.authority().toArray(new String[0]));
                    }
                    auth.anyRequest().denyAll();
                })
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails gateway = User.builder()
                .username("client_id")
                .password("{noop}client_secret")
                .authorities(EndpointRuleProvider.SCOPE_PREFIX + Permission.TOKEN_INTROSPECT.getPermission())
                .build();

        return new InMemoryUserDetailsManager(gateway);
    }

}
