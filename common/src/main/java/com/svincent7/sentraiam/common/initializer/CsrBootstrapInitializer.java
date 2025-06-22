package com.svincent7.sentraiam.common.initializer;

import com.svincent7.sentraiam.common.cert.CsrUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CsrBootstrapInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final @NonNull ConfigurableApplicationContext applicationContext) {
        CsrBootstrapProperty property = new CsrBootstrapProperty(applicationContext);
        if (property.getKeystorePath() == null || property.getTrustStorePath() == null) {
            return;
        }

        File ksFile = new File(property.getKeystorePath());
        File tsFile = new File(property.getTrustStorePath());
        if (ksFile.exists()) {
            log.debug("Key file exists. Skipping cert generation.");
            enableSSL(property, ksFile, tsFile);
            return;
        }

        try {
            log.debug("🔐 Generating key pair and CSR...");
            KeyPair keyPair = CsrUtil.generateKeyPair();
            var csr = CsrUtil.createCsr(keyPair, property.getCaSubjectDn(),
                    List.of(property.getDnsNames()), List.of(property.getIpAddresses()));

            log.debug("📡 Requesting cert from CA...");
            X509Certificate cert = CsrUtil.fetchCertFromCa(property.getCaServerUrl(),
                    property.getBootstrapCaToken(), csr);

            log.debug("📡 Getting trusted cert from CA...");
            X509Certificate trustCert = CsrUtil.fetchTrustStore(property.getCaServerUrl(),
                    property.getBootstrapCaToken());

            log.debug("💾 Saving keystore...");
            log.debug("Issuer: {}", cert.getIssuerX500Principal());
            log.debug("Subject: {}", trustCert.getSubjectX500Principal());
            CsrUtil.storeToKeystore(ksFile, keyPair, new X509Certificate[]{cert, trustCert},
                    Objects.requireNonNull(property.getKeystoreAlias()),
                    Objects.requireNonNull(property.getKeystorePassword()));

            CsrUtil.storeToTrustStore(tsFile, trustCert, Objects.requireNonNull(property.getTrustStoreAlias()),
                    Objects.requireNonNull(property.getTrustStorePassword()));

            log.debug("✅ Eureka certificate ready!");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to bootstrap cert", e);
        }
        enableSSL(property, ksFile, tsFile);
    }

    private void enableSSL(final CsrBootstrapProperty property, final File ksFile, final File tsFile) {
        System.setProperty("eureka.client.tls.enabled", "true");
        System.setProperty("eureka.client.tls.key-store", "file:" + ksFile.getAbsolutePath());
        System.setProperty("eureka.client.tls.key-store-type", CsrUtil.KEYSTORE_TYPE);
        System.setProperty("eureka.client.tls.key-password", property.getKeystorePassword());
        System.setProperty("eureka.client.tls.key-store-password", property.getKeystorePassword());
        System.setProperty("eureka.client.tls.keyAlias", property.getKeystoreAlias());
        System.setProperty("eureka.client.tls.trust-store", "file:" + tsFile.getAbsolutePath());
        System.setProperty("eureka.client.tls.trust-store-password", property.getTrustStorePassword());
        System.setProperty("eureka.client.tls.trust-store-type", CsrUtil.KEYSTORE_TYPE);

        if (property.isEnableServerSSL()) {
            System.setProperty("server.ssl.enabled", "true");
            System.setProperty("server.ssl.key-store", property.getKeystorePath());
            System.setProperty("server.ssl.key-alias", property.getKeystoreAlias());
            System.setProperty("server.ssl.key-password", property.getKeystorePassword());
            System.setProperty("server.ssl.key-store-password", property.getKeystorePassword());
            System.setProperty("server.ssl.key-store-type", CsrUtil.KEYSTORE_TYPE);
            System.setProperty("server.ssl.client-auth", "need");
            System.setProperty("server.ssl.trust-store", property.getTrustStorePath());
            System.setProperty("server.ssl.trust-store-password", property.getTrustStorePassword());
            System.setProperty("server.ssl.trust-store-type", CsrUtil.KEYSTORE_TYPE);
        }
    }

    @Getter
    @ToString
    static final class CsrBootstrapProperty {
        private final String bootstrapCaToken;
        private final String keystorePath;
        private final String keystoreAlias;
        private final String keystorePassword;
        private final String caServerUrl;
        private final String caSubjectDn;
        private final String trustStorePath;
        private final String trustStoreAlias;
        private final String trustStorePassword;
        private final String[] dnsNames;
        private final String[] ipAddresses;
        private final boolean enableServerSSL;

        private CsrBootstrapProperty(final ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            this.bootstrapCaToken = env.getProperty("com.svincent7.sentraiam.cert.bootstrap-ca-token");
            this.keystorePath = env.getProperty("com.svincent7.sentraiam.cert.keystore-path");
            this.keystoreAlias = env.getProperty("com.svincent7.sentraiam.cert.keystore-alias");
            this.keystorePassword = env.getProperty("com.svincent7.sentraiam.cert.keystore-password");
            this.caServerUrl = env.getProperty("com.svincent7.sentraiam.cert.ca-server-url");
            this.caSubjectDn = env.getProperty("com.svincent7.sentraiam.cert.ca-subject-dn");
            this.trustStorePath = env.getProperty("com.svincent7.sentraiam.cert.truststore-path");
            this.trustStoreAlias = env.getProperty("com.svincent7.sentraiam.cert.truststore-alias");
            this.trustStorePassword = env.getProperty("com.svincent7.sentraiam.cert.truststore-password");
            this.dnsNames = env.getProperty("com.svincent7.sentraiam.cert.dns-names", "").split(",");
            this.ipAddresses = env.getProperty("com.svincent7.sentraiam.cert.ip-addresses", "").split(",");
            this.enableServerSSL = Boolean.parseBoolean(
                    env.getProperty("com.svincent7.sentraiam.cert.enable-server-ssl", "false"));

            log.debug("CsrBootstrapProperty: {}", this);
        }
    }
}
