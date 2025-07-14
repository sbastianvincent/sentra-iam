package com.svincent7.sentraiam.identity.config;

import org.mockito.Mockito;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.boot.ssl.SslManagerBundle;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfigTests {

    @Bean
    public SslBundles sslBundles() {
        SslBundles sslBundles = Mockito.mock(SslBundles.class);
        SslBundle sslBundle = Mockito.mock(SslBundle.class);
        Mockito.when(sslBundle.createSslContext()).thenReturn(Mockito.mock(javax.net.ssl.SSLContext.class));
        Mockito.when(sslBundles.getBundle(Mockito.any())).thenReturn(sslBundle);
        Mockito.when(sslBundle.getManagers()).thenReturn(Mockito.mock(SslManagerBundle.class));
        return sslBundles;
    }

    @Bean
    public RestTemplateBuilder builder() {
        RestTemplateBuilder restTemplateBuilder = Mockito.mock(RestTemplateBuilder.class);
        Mockito.when(restTemplateBuilder.sslBundle(Mockito.any())).thenReturn(restTemplateBuilder);
        Mockito.when(restTemplateBuilder.build()).thenReturn(Mockito.mock(org.springframework.web.client.RestTemplate.class));
        return restTemplateBuilder;
    }
}