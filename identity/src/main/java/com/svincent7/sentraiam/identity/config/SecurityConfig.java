package com.svincent7.sentraiam.identity.config;

import com.svincent7.sentraiam.common.cert.SSLBundleEurekaClientHttpRequestFactorySupplier;
import com.svincent7.sentraiam.common.config.ConfigProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public EurekaClientHttpRequestFactorySupplier eurekaClientHttpRequestFactorySupplier(final ConfigProperties config,
                                                                                         final SslBundles sslBundles) {
        return new SSLBundleEurekaClientHttpRequestFactorySupplier(config, sslBundles);
    }
}
