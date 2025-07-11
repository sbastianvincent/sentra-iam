package com.svincent7.sentraiam.identity.config;

import com.svincent7.sentraiam.common.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.common.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.common.cert.SSLBundleEurekaClientHttpRequestFactorySupplier;
import com.svincent7.sentraiam.common.config.ConfigProperties;
import com.svincent7.sentraiam.common.permission.Permission;
import com.svincent7.sentraiam.identity.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
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
                        auth.requestMatchers(rule.method(), rule.path()).hasAnyAuthority(
                                rule.authority().toArray(new String[0]));
                    }
                    auth.anyRequest().denyAll();
                })
                .addFilterBefore(new AuthenticationFilter(), X509AuthenticationFilter.class)
                .x509(x509 -> {
                    x509.subjectPrincipalRegex("CN=(.*?)(?:,|$)").userDetailsService(userDetailsService());
                });

        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails gateway = User.builder()
                .username("sentra-iam-auth")
                .password("noop-password")
                .authorities(
                    EndpointRuleProvider.SCOPE_PREFIX + Permission.CREDENTIALS_VERIFY.getPermission(),
                    EndpointRuleProvider.SCOPE_PREFIX + Permission.USERS_GET.getPermission()
                )
                .build();

        return new InMemoryUserDetailsManager(gateway);
    }

    @Bean
    public EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier(final ConfigProperties config,
                                                                                         final SslBundles sslBundles) {
        return new SSLBundleEurekaClientHttpRequestFactorySupplier(config, sslBundles);
    }
}
