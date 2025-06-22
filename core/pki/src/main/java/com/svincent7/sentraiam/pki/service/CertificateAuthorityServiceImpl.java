package com.svincent7.sentraiam.pki.service;

import com.svincent7.sentraiam.common.cert.CsrUtil;
import com.svincent7.sentraiam.pki.config.PkiConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaContentVerifierProviderBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CertificateAuthorityServiceImpl implements CertificateAuthorityService {
    private static final int SERIAL_LENGTH = 64;

    private final PkiConfig config;
    private PrivateKey caPrivateKey;
    private X509Certificate caCert;

    @PostConstruct
    void init() {
        if (config.isSkipRootCaCheck()) {
            return;
        }

        Security.addProvider(new BouncyCastleProvider());

        try {
            final File file = new File(config.getRootCaPath());
            if (config.isForceRegenerateRootCa()) {
                log.warn("Forcing regenerate Root CA");
                generateRootCA(file);
            } else if (!file.exists()) {
                log.warn("Root CA does not exist. Generating new one");
                generateRootCA(file);
            }
            loadRootCA(file);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to initialize Root CA", exception);
        }
    }

    @Override
    public String signCsr(final byte[] csrBytes) {
        try {
            // 1. Parse PEM CSR
            PKCS10CertificationRequest csr = new PKCS10CertificationRequest(csrBytes);

            if (!csr.isSignatureValid(new JcaContentVerifierProviderBuilder().build(csr.getSubjectPublicKeyInfo()))) {
                throw new IllegalArgumentException("CSR signature invalid");
            }

            X500Name subject = csr.getSubject();
            Pair<Date, Date> validityDatePair = getValidityDatePair(false);
            X500Name caIssuerName = new X500Name(caCert.getSubjectX500Principal().getName());

            X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                    caIssuerName,
                    getSerial(),
                    validityDatePair.getFirst(),
                    validityDatePair.getSecond(),
                    subject,
                    csr.getSubjectPublicKeyInfo()
            );

            Attribute[] attributes = csr.getAttributes(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest);
            if (attributes != null) {
                for (Attribute attr : attributes) {
                    ASN1Encodable[] values = attr.getAttributeValues();
                    for (ASN1Encodable value : values) {
                        if (value instanceof Extensions) {
                            Extensions extensions = (Extensions) value;

                            GeneralNames subjectAltNames = GeneralNames.fromExtensions(extensions,
                                    Extension.subjectAlternativeName);
                            // Do something with subjectAltNames

                            if (subjectAltNames != null) {
                                // Add the SAN extension from the CSR to the certificate builder.
                                // Maintain the criticality from the CSR if you want, or enforce 'true'.
                                // For SANs, 'true' is highly recommended.
                                certBuilder.addExtension(
                                        Extension.subjectAlternativeName,
                                        true, // Force critical for SANs as best practice
                                        subjectAltNames
                                );
                                // Assuming only one SAN extension is expected in the CSR's extensionRequest
                                break; // Exit inner loop once SANs are found and added
                            }
                        } else {
                            // Try to convert it if it's wrapped in a DLSet
                            ASN1Encodable maybeExtensions = ((ASN1Set) value).getObjectAt(0);
                            Extensions extensions = Extensions.getInstance(maybeExtensions);

                            GeneralNames subjectAltNames = GeneralNames.fromExtensions(extensions,
                                    Extension.subjectAlternativeName);
                            // Do something with subjectAltNames

                            if (subjectAltNames != null) {
                                // Add the SAN extension from the CSR to the certificate builder.
                                // Maintain the criticality from the CSR if you want, or enforce 'true'.
                                // For SANs, 'true' is highly recommended.
                                certBuilder.addExtension(
                                        Extension.subjectAlternativeName,
                                        true, // Force critical for SANs as best practice
                                        subjectAltNames
                                );
                                // Assuming only one SAN extension is expected in the CSR's extensionRequest
                                break; // Exit inner loop once SANs are found and added
                            }
                        }
                    }
                }
            }

            // Add Key Usage: Digital Signature and Key Encipherment
            // These are commonly needed for both client and server certificates.
            certBuilder.addExtension(
                    Extension.keyUsage,
                    true, // Criticality
                    new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment)
            );

            // Add Extended Key Usage: BOTH Client Authentication and Server Authentication
            // This is the key change to make certificates usable for both roles.
            certBuilder.addExtension(
                    Extension.extendedKeyUsage,
                    false, // Criticality
                    new ExtendedKeyUsage(new KeyPurposeId[]{KeyPurposeId.id_kp_clientAuth,
                            KeyPurposeId.id_kp_serverAuth})
            );

            ContentSigner signer = new JcaContentSignerBuilder(CsrUtil.SIGNING_ALGORITHM)
                    .build(caPrivateKey);

            X509CertificateHolder certHolder = certBuilder.build(signer);

            // 3. Convert to X509Certificate
            X509Certificate cert = new JcaX509CertificateConverter()
                    .getCertificate(certHolder);

            // 4. Write as PEM
            try (StringWriter sw = new StringWriter();
                 JcaPEMWriter pemWriter = new JcaPEMWriter(sw)) {
                pemWriter.writeObject(cert);
                pemWriter.flush();
                return sw.toString();
            }

        } catch (Exception e) {
            log.error("Failed to sign CSR", e);
            throw new RuntimeException("Failed to sign CSR", e);
        }
    }

    @Override
    public X509Certificate getRootCertificate() {
        return caCert;
    }

    private void generateRootCA(final File file) throws Exception {
        KeyPair keyPair = CsrUtil.generateKeyPair();
        X500Name issuer = new X500Name(config.getRootCaCommonName());
        Pair<Date, Date> validityDatePair = getValidityDatePair(true);

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(issuer, getSerial(),
                validityDatePair.getFirst(), validityDatePair.getSecond(), issuer, keyPair.getPublic());

        certificateBuilder.addExtension(
                Extension.basicConstraints,
                true,
                new BasicConstraints(true)
        );

        ContentSigner signer = new JcaContentSignerBuilder(CsrUtil.SIGNING_ALGORITHM).build(keyPair.getPrivate());

        X509Certificate cert = new JcaX509CertificateConverter().getCertificate(certificateBuilder.build(signer));
        cert.verify(keyPair.getPublic());

        CsrUtil.storeToKeystore(file, keyPair, new X509Certificate[]{cert}, config.getRootCaAlias(),
                config.getRootCaPassword());

        log.debug("Root CA generated successfully");
        log.debug("Serial: {}", cert.getSerialNumber());
        log.debug("Not Before: {}", cert.getNotBefore());
        log.debug("Not After: {}", cert.getNotAfter());
        log.debug("Signature Algorithm: {}", cert.getSigAlgName());
        log.debug("Public Key Algorithm: {}", cert.getPublicKey().getAlgorithm());
        log.debug("Public Key Format: {}", cert.getPublicKey().getFormat());
        log.debug("Public Key: {}", cert.getPublicKey());
        log.debug("Signature: {}", cert.getSignature());
        log.debug("Certificate: {}", cert);
    }

    private void loadRootCA(final File file) throws Exception {
        KeyStore ks = KeyStore.getInstance(CsrUtil.KEYSTORE_TYPE);
        ks.load(new FileInputStream(file), config.getRootCaPassword().toCharArray());

        this.caPrivateKey = (PrivateKey) ks.getKey(config.getRootCaAlias(), config.getRootCaPassword().toCharArray());
        this.caCert = (X509Certificate) ks.getCertificate(config.getRootCaAlias());

        log.debug("✅ Loaded existing Root CA certificate.");
    }

    private BigInteger getSerial() {
        return new BigInteger(SERIAL_LENGTH, new SecureRandom());
    }

    private Pair<Date, Date> getValidityDatePair(final boolean isRootCA) {
        int days = isRootCA ? config.getRootCaValidityDays() : config.getCertificateValidityDays();
        Date notBefore = new Date(System.currentTimeMillis());
        Date notAfter = new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(days));
        return Pair.of(notBefore, notAfter);
    }
}
