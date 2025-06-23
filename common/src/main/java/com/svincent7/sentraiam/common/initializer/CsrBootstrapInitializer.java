package com.svincent7.sentraiam.common.initializer;

import com.svincent7.sentraiam.common.cert.CsrUtil;
import io.micrometer.common.util.StringUtils;
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

@Slf4j
public class CsrBootstrapInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final @NonNull ConfigurableApplicationContext applicationContext) {
        CsrBootstrapProperty property = new CsrBootstrapProperty(applicationContext);
        if (StringUtils.isEmpty(property.getKeystorePath()) || StringUtils.isEmpty(property.getTrustStorePath())) {
            return;
        }

        File ksFile = new File(property.getKeystorePath());
        File tsFile = new File(property.getTrustStorePath());
        if (ksFile.exists() && !property.isRecreateKeys()) {
            log.debug("Key file exists. Skipping cert generation.");
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
                    property.getKeystoreAlias(), property.getKeystorePassword());

            CsrUtil.storeToTrustStore(tsFile, trustCert, property.getTrustStoreAlias(),
                    property.getTrustStorePassword());

            log.debug("✅ Eureka certificate ready!");

        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to bootstrap cert", e);
        }
    }

    @Getter
    @ToString
    static final class CsrBootstrapProperty {
        private final boolean recreateKeys;
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

        private CsrBootstrapProperty(final ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment env = applicationContext.getEnvironment();
            this.recreateKeys = env.getProperty("com.svincent7.sentraiam.cert.recreate-keys", Boolean.class, false);
            this.bootstrapCaToken = env.getProperty("com.svincent7.sentraiam.cert.bootstrap-ca-token", "");
            this.keystorePath = env.getProperty("com.svincent7.sentraiam.cert.keystore-path", "");
            this.keystoreAlias = env.getProperty("com.svincent7.sentraiam.cert.keystore-alias", "");
            this.keystorePassword = env.getProperty("com.svincent7.sentraiam.cert.keystore-password", "");
            this.caServerUrl = env.getProperty("com.svincent7.sentraiam.cert.ca-server-url", "");
            this.caSubjectDn = env.getProperty("com.svincent7.sentraiam.cert.ca-subject-dn", "");
            this.trustStorePath = env.getProperty("com.svincent7.sentraiam.cert.truststore-path", "");
            this.trustStoreAlias = env.getProperty("com.svincent7.sentraiam.cert.truststore-alias", "");
            this.trustStorePassword = env.getProperty("com.svincent7.sentraiam.cert.truststore-password", "");
            this.dnsNames = env.getProperty("com.svincent7.sentraiam.cert.dns-names", "").split(",");
            this.ipAddresses = env.getProperty("com.svincent7.sentraiam.cert.ip-addresses", "").split(",");

            log.debug("CsrBootstrapProperty: {}", this);
        }
    }
}
