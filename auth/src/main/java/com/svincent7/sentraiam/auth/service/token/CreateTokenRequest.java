package com.svincent7.sentraiam.auth.service.token;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;

import java.util.Map;

@Data
public class CreateTokenRequest {
    private String id;
    private String issuer;
    private String subject;
    private long issuedAt;
    private long expiration;
    private Map<String, Object> additionalData;
    private SignatureAlgorithm signatureAlgorithm;
    private String secret;
}
