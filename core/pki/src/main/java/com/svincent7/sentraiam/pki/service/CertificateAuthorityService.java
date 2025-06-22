package com.svincent7.sentraiam.pki.service;

import java.security.cert.X509Certificate;

public interface CertificateAuthorityService {
    String signCsr(byte[] csrBytes);
    X509Certificate getRootCertificate();
}
