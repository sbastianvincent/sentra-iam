package com.svincent7.sentraiam.common.cert;

import com.svincent7.sentraiam.common.config.ConfigProperties;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.io.SocketConfig;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.util.concurrent.TimeUnit;

public class SSLBundleEurekaClientHttpRequestFactorySupplier implements EurekaClientHttpRequestFactorySupplier {
    private final ConfigProperties properties;
    private final SslBundles sslBundles;

    private static final int SOCKET_TIMEOUT_SECONDS = 10;
    private static final int CONNECT_TIMEOUT_MS = 5000;
    private static final int READ_TIMEOUT_MS = 5000;

    public SSLBundleEurekaClientHttpRequestFactorySupplier(final ConfigProperties propertiesInput,
                                                           final SslBundles sslBundlesInput) {
        this.properties = propertiesInput;
        this.sslBundles = sslBundlesInput;
    }

    @Override
    public ClientHttpRequestFactory get(final SSLContext sslContext, final HostnameVerifier hostnameVerifier) {
        var socketConfig = SocketConfig.custom()
                .setSoTimeout(SOCKET_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        SslBundle bundle = sslBundles.getBundle(properties.getSslBundleName());

        TlsSocketStrategy tlsStrategy = new DefaultClientTlsStrategy(
                bundle.createSslContext(),
                hostnameVerifier
        );

        var connManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsStrategy)
                .setDefaultSocketConfig(socketConfig)
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

        var factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        factory.setReadTimeout(READ_TIMEOUT_MS);
        return factory;
    }
}
