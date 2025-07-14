package com.svincent7.sentraiam.identity.config;

import com.svincent7.sentraiam.identity.auth.endpoint.EndpointRule;
import com.svincent7.sentraiam.identity.auth.endpoint.EndpointRuleProvider;
import com.svincent7.sentraiam.identity.filter.AuthenticationFilter;
import com.svincent7.sentraiam.identity.permission.Permission;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.x509.X509AuthenticationFilter;
import org.springframework.web.client.RestOperations;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final EndpointRuleProvider endpointRuleProvider;
    private final IdentityConfig config;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity, final RestTemplateBuilder builder,
                                                   final SslBundles sslBundles) throws Exception {
        httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(endpointRuleProvider.getPermittedEndpoints()).permitAll();
                    for (EndpointRule rule : endpointRuleProvider.getEndpointRules()) {
                        auth.requestMatchers(rule.method(), rule.path()).hasAnyAuthority(
                                rule.authority().toArray(new String[0]));
                    }
                    auth.anyRequest().authenticated();
                })
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                                .decoder(jwtDecoder(builder, sslBundles))))
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
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("SCOPE_");
        authoritiesConverter.setAuthoritiesClaimName("scopes");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

    @Bean
    public RestOperations customRestOperations(final RestTemplateBuilder builder, final SslBundles sslBundles) {
        return builder.sslBundle(sslBundles.getBundle(config.getSslBundleName())).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(final RestTemplateBuilder builder, final SslBundles sslBundles) {
        return new SentraIamJwtDecoder(customRestOperations(builder, sslBundles));
    }
}
