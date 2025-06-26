package com.svincent7.sentraiam.apigateway.config;

import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SentraIamJwtDecoder implements ReactiveJwtDecoder {

    private final Map<String, ReactiveJwtDecoder> decoderCache = new ConcurrentHashMap<>();
    private final WebClient webClient;

    public SentraIamJwtDecoder(final WebClient webClientInput) {
        this.webClient = webClientInput;
    }

    @Override
    public Mono<Jwt> decode(final String token) throws JwtException {
        return Mono.fromCallable(() -> {
            var jwt = JWTParser.parse(token);
            var claims = jwt.getJWTClaimsSet();
            String issuer = claims.getIssuer();
            ReactiveJwtDecoder decoder = resolveDecoder(webClient, issuer);
            return decoder.decode(token);
        }).flatMap(mono -> mono)
                .doOnError(e -> log.error("JWT Error", e));
    }

    private ReactiveJwtDecoder resolveDecoder(final WebClient client, final String issuer) {
        return decoderCache.computeIfAbsent(issuer, iss -> NimbusReactiveJwtDecoder.withIssuerLocation(issuer)
                .webClient(client)
                .build());
    }
}
