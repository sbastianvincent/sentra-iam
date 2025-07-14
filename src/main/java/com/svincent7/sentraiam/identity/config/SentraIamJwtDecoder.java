package com.svincent7.sentraiam.identity.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SentraIamJwtDecoder implements JwtDecoder {

    private final Map<String, JwtDecoder> decoderCache = new ConcurrentHashMap<>();
    private final RestOperations restOperations;

    public SentraIamJwtDecoder(final RestOperations restOps) {
        this.restOperations = restOps;
    }

    @Override
    public Jwt decode(final String token) throws JwtException {
        try {
            var jwt = JWTParser.parse(token);
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            String issuer = claims.getIssuer();

            JwtDecoder decoder = resolveDecoder(issuer);
            return decoder.decode(token);

        } catch (Exception e) {
            throw new JwtException("Failed to decode JWT", e);
        }
    }

    JwtDecoder resolveDecoder(final String issuer) {
        return decoderCache.computeIfAbsent(issuer, iss ->
                NimbusJwtDecoder
                        .withIssuerLocation(iss)
                        .restOperations(restOperations)
                        .build()
        );
    }
}
