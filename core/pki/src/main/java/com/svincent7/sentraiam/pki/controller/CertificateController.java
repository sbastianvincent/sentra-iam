package com.svincent7.sentraiam.pki.controller;

import com.svincent7.sentraiam.pki.service.CertificateAuthorityService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping("/api/v1/certificates")
@RequiredArgsConstructor
@Slf4j
public class CertificateController {
    private final CertificateAuthorityService certificateAuthorityService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/pkcs10")
    public ResponseEntity<String> issueCertificate(final @RequestBody byte[] csrBytes) {
        try {
            String cert = certificateAuthorityService.signCsr(csrBytes);
            return ResponseEntity.ok(cert);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("CSR Error: " + e.getMessage());
        }
    }

    @GetMapping(value = "/ca.pem", produces = MediaType.TEXT_PLAIN_VALUE)
    public void downloadCaCertificate(final HttpServletResponse response) {
        try {
            X509Certificate caCert = certificateAuthorityService.getRootCertificate();
            response.setContentType("application/x-pem-file");
            response.setHeader("Content-Disposition", "attachment; filename=ca.pem");

            try (PrintWriter writer = response.getWriter();
                 JcaPEMWriter pemWriter = new JcaPEMWriter(writer)) {
                pemWriter.writeObject(caCert);
                pemWriter.flush();
            }

        } catch (Exception e) {
            log.error("❌ Failed to return CA certificate", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
