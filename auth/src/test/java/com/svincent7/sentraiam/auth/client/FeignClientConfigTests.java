package com.svincent7.sentraiam.auth.client;

import com.svincent7.sentraiam.auth.config.SentraIamAuthConfig;
import org.mockito.Mockito;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfigTests {

    @Bean
    public SslBundles sslBundles() {
        SslBundles sslBundles = Mockito.mock(SslBundles.class);
        SslBundle sslBundle = Mockito.mock(SslBundle.class);
        Mockito.when(sslBundle.createSslContext()).thenReturn(Mockito.mock(javax.net.ssl.SSLContext.class));
        Mockito.when(sslBundles.getBundle("test")).thenReturn(sslBundle);
        return sslBundles;
    }

    @Bean
    public SentraIamAuthConfig sentraIamAuthConfig() {
        SentraIamAuthConfig configProperties = Mockito.mock(SentraIamAuthConfig.class);
        Mockito.when(configProperties.getSslBundleName()).thenReturn("test");
        return configProperties;
    }
}
