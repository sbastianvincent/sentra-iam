package com.svincent7.sentraiam.auth.service.token;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenGenerator implements TokenGenerator {

    @Override
    public String generateToken(final CreateTokenRequest createTokenRequest) {
        return Jwts.builder()
                .setId(createTokenRequest.getId())
                .setSubject(createTokenRequest.getSubject())
                .setIssuedAt(Date.from(Instant.ofEpochMilli(createTokenRequest.getIssuedAt())))
                .setExpiration(Date.from(Instant.ofEpochMilli(createTokenRequest.getExpiration())))
                .setIssuer(createTokenRequest.getIssuer())
                .addClaims(createTokenRequest.getAdditionalData())
                .signWith(createTokenRequest.getSignatureAlgorithm(), createTokenRequest.getSecret())
                .compact();
    }
}
