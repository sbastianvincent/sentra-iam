package com.svincent7.sentraiam.apigateway.config;

import com.svincent7.sentraiam.common.cert.SSLBundleEurekaClientHttpRequestFactorySupplier;
import com.svincent7.sentraiam.common.config.ConfigProperties;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringReactiveOpaqueTokenIntrospector;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private static final String[] PERMIT_LIST = {
        "/api/auth/v1/login",
        "/api/auth/v1/refresh",
        "/api/auth/v1/logout",
        "/api/auth/v1/introspect",
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
            final ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(PERMIT_LIST)
                        .permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(oauth2 ->
                        oauth2.opaqueToken(Customizer.withDefaults()));

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
    public ReactiveOpaqueTokenIntrospector introspector(final ApiGatewayConfig config, final SslBundles sslBundles) {
        HttpClient httpClient = httpClient(config, sslBundles);
        WebClient customWebClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        return new SpringReactiveOpaqueTokenIntrospector(
                config.getIntrospectionUri(),
                customWebClient
        );
    }
}
