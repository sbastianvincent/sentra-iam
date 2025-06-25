package com.svincent7.sentraiam.apigateway.config;

import com.svincent7.sentraiam.common.cert.SSLBundleEurekaClientHttpRequestFactorySupplier;
import com.svincent7.sentraiam.common.config.ConfigProperties;
import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.WebFilter;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {
    private static final String[] PERMIT_LIST = {
        "/api/auth/v1/login",
        "/api/auth/v1/refresh",
        "/api/auth/v1/logout",
        "/api/auth/v1/keys/**"
    };

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            final ServerHttpSecurity serverHttpSecurity, final ConfigProperties config, final SslBundles sslBundles) {
        serverHttpSecurity
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PERMIT_LIST)
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtDecoder(
                                new SentraIamJwtDecoder(webClient(httpClient(config, sslBundles))))));

        return serverHttpSecurity.build();
    }

    @Bean
    public EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier(final ConfigProperties config,
                                                                                         final SslBundles sslBundles) {
        return new SSLBundleEurekaClientHttpRequestFactorySupplier(config, sslBundles);
    }

    @Bean
    public HttpClient httpClient(final ConfigProperties config, final SslBundles sslBundles) {
        SslBundle sslBundle = sslBundles.getBundle(config.getSslBundleName());
        SslContextBuilder sslContextBuilder = SslContextBuilder.forClient()
                .keyManager(sslBundle.getManagers().getKeyManagerFactory())
                .trustManager(sslBundle.getManagers().getTrustManagerFactory());

        SslContext sslContext;
        try {
            sslContext = sslContextBuilder.build();
        } catch (SSLException e) {
            throw new IllegalStateException("Failed to build SSL context", e);
        }

        return HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
    }

    @Bean
    public WebClient webClient(final HttpClient httpClient) {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public WebFilter authHeaderInjector() {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .flatMap(context -> {
                    var auth = context.getAuthentication();

                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                        String userId = jwtAuth.getName();
                        String tenantId = jwtAuth.getToken().getClaim(TokenConstant.TENANT_ID);
                        String username = jwtAuth.getToken().getClaim(TokenConstant.USERNAME);
                        String roles = jwtAuth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(","));

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-Id", userId)
                                .header("X-Username", username)
                                .header("X-Tenant-Id", tenantId)
                                .header("X-Roles", roles)
                                .build();

                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }

                    // If not JWT auth, just continue
                    return chain.filter(exchange);
                })
                .switchIfEmpty(chain.filter(exchange)); // Handle when no security context
    }
}
