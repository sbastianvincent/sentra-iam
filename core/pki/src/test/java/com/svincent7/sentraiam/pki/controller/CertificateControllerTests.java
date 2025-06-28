package com.svincent7.sentraiam.pki.controller;

import com.svincent7.sentraiam.pki.service.CertificateAuthorityService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.PrintWriter;

public class CertificateControllerTests {
    private CertificateAuthorityService certificateAuthorityService;
    private CertificateController certificateController;

    @BeforeEach
    void setup() {
        this.certificateAuthorityService = Mockito.mock(CertificateAuthorityService.class);
        this.certificateController = new CertificateController(certificateAuthorityService);
    }

    @Test
    void testIssueCertificate() {
        byte[] csrBytes = new byte[0];

        Mockito.when(certificateAuthorityService.signCsr(csrBytes)).thenReturn("certificates");

        ResponseEntity<String> response = certificateController.issueCertificate(csrBytes);

        Assertions.assertEquals(response.getStatusCodeValue(), 200);
        Assertions.assertEquals(response.getBody(), "certificates");
    }

    @Test
    void testIssueCertificate_ThrowException() {
        byte[] csrBytes = new byte[0];

        Mockito.when(certificateAuthorityService.signCsr(csrBytes)).thenThrow(new RuntimeException("test exception"));

        ResponseEntity<String> response = certificateController.issueCertificate(csrBytes);

        Assertions.assertEquals(response.getStatusCodeValue(), 400);
        Assertions.assertEquals(response.getBody(), "CSR Error: test exception");
    }

    @Test
    void testDownloadCaCertificate() throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        Mockito.when(certificateAuthorityService.getRootCertificate()).thenReturn(Mockito.mock(java.security.cert.X509Certificate.class));
        Mockito.when(response.getWriter()).thenReturn(Mockito.mock(PrintWriter.class));

        certificateController.downloadCaCertificate(response);
    }

    @Test
    void testDownloadCaCertificate_ThrowException() throws IOException {
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(response.getWriter()).thenThrow(new IOException("test exception"));
        certificateController.downloadCaCertificate(response);
    }
}
