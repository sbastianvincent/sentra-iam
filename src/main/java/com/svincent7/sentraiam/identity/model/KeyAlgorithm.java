package com.svincent7.sentraiam.identity.model;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum KeyAlgorithm {
    HS256(SignatureAlgorithm.HS256, "HMAC_SHA256"),  // HMAC with SHA-256
    HS384(SignatureAlgorithm.HS384, "HMAC_SHA384"),  // HMAC with SHA-384
    HS512(SignatureAlgorithm.HS512, "HMAC_SHA512"),  // HMAC with SHA-512
    RS256(SignatureAlgorithm.RS256, "RSA_SHA256"),   // RSA with SHA-256
    RS384(SignatureAlgorithm.RS384, "RSA_SHA384"),   // RSA with SHA-384
    RS512(SignatureAlgorithm.RS512, "RSA_SHA512"),   // RSA with SHA-512
    ES256(SignatureAlgorithm.ES256, "ECDSA_SHA256"), // ECDSA with SHA-256
    ES384(SignatureAlgorithm.ES384, "ECDSA_SHA384"), // ECDSA with SHA-384
    ES512(SignatureAlgorithm.ES512, "ECDSA_SHA512"), // ECDSA with SHA-512
    PS256(SignatureAlgorithm.PS256, "PSA_SHA256"),   // RSASSA-PSS with SHA-256
    PS384(SignatureAlgorithm.PS384, "PSA_SHA384"),   // RSASSA-PSS with SHA-384
    PS512(SignatureAlgorithm.PS512, "PSA_SHA512");   // RSASSA-PSS with SHA-512

    private final SignatureAlgorithm signatureAlgorithm;
    private final String description;
}
