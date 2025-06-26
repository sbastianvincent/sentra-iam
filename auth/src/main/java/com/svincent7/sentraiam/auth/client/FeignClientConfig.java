package com.svincent7.sentraiam.auth.client;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import feign.Client;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import java.util.List;

@Configuration
public class FeignClientConfig {

    @Bean
    public Client feignClient(final List<LoadBalancerFeignRequestTransformer> transformers,
                              final LoadBalancerClientFactory loadBalancerClientFactory,
                              final LoadBalancerClient loadBalancerClient,
                              final ConfigProperties config, final SslBundles sslBundles) {
        SslBundle bundle = sslBundles.getBundle(config.getSslBundleName());

        SSLContext sslContext = bundle.createSslContext();

        Client client = new Client.Default(sslContext.getSocketFactory(),
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        return new FeignBlockingLoadBalancerClient(client, loadBalancerClient,
                loadBalancerClientFactory, transformers);
    }
}
