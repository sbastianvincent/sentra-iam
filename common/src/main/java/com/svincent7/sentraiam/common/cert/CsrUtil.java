package com.svincent7.sentraiam.common.cert;

import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Attribute;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public final class CsrUtil {
    public static final String KEYSTORE_TYPE = "PKCS12";
    public static final String SIGNING_ALGORITHM = "SHA256withRSA";

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_LENGTH = 4096;

    private CsrUtil() {
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyGen.initialize(KEY_LENGTH);
        return keyGen.generateKeyPair();
    }

    public static PKCS10CertificationRequest createCsr(final KeyPair keyPair, final String subjectDN,
                                                       final List<String> dnsNames,
                                                       final List<String> ipAddresses) throws Exception {
        X500Name subject = new X500Name(subjectDN);
        JcaPKCS10CertificationRequestBuilder csrBuilder = new JcaPKCS10CertificationRequestBuilder(
                subject, keyPair.getPublic());

        // Build SAN extension
        List<GeneralName> names = new ArrayList<>();
        for (String dns : dnsNames) {
            names.add(new GeneralName(GeneralName.dNSName, dns));
        }
        for (String ip : ipAddresses) {
            names.add(new GeneralName(GeneralName.iPAddress, ip));
        }
        GeneralNames subjectAltNames = new GeneralNames(names.toArray(new GeneralName[0]));

        ExtensionsGenerator extGen = new ExtensionsGenerator();
        extGen.addExtension(Extension.subjectAlternativeName, false, subjectAltNames);
        Extensions extensions = extGen.generate();

        Attribute attribute = new Attribute(
                PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                new DERSet(extensions)
        );

        csrBuilder.addAttribute(attribute.getAttrType(), attribute.getAttrValues());
        ContentSigner signer = new JcaContentSignerBuilder(SIGNING_ALGORITHM).build(keyPair.getPrivate());
        return csrBuilder.build(signer);
    }

    public static X509Certificate fetchCertFromCa(final String caUrl, final String token,
                                                  final PKCS10CertificationRequest csr) throws Exception {
        String caUrlRequest = caUrl + "/api/v1/certificates";
        HttpURLConnection conn = (HttpURLConnection) new URL(caUrlRequest).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestProperty("Content-Type", "application/pkcs10");
        conn.setDoOutput(true);
        conn.getOutputStream().write(csr.getEncoded());

        try (InputStream is = conn.getInputStream()) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        }
    }

    public static X509Certificate fetchTrustStore(final String rootCertUrl, final String token) throws Exception {
        String caUrlRequest = rootCertUrl + "/api/v1/certificates/ca.pem";
        HttpURLConnection conn = (HttpURLConnection) new URL(caUrlRequest).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);

        try (InputStream is = conn.getInputStream()) {
            return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(is);
        }
    }

    public static void storeToKeystore(final File file, final KeyPair keyPair, final X509Certificate[] chain,
                                       final String alias, final String password) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
        ks.load(null);
        ks.setKeyEntry(alias, keyPair.getPrivate(), password.toCharArray(), chain);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ks.store(fos, password.toCharArray());
        }
    }

    public static void storeToTrustStore(final File file, final X509Certificate cert, final String alias,
                                         final String password) throws Exception {
        KeyStore ks = KeyStore.getInstance(KEYSTORE_TYPE);
        ks.load(null);
        ks.setCertificateEntry(alias, cert);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ks.store(fos, password.toCharArray());
        }
    }
}
