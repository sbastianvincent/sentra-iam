package com.svincent7.sentraiam.identity.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.client.RestOperations;

import java.text.ParseException;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SentraIamJwtDecoderTests {
    private RestOperations restOperations;
    private SentraIamJwtDecoder sentraIamJwtDecoder;

    @BeforeEach
    void setUp() {
        restOperations = Mockito.mock(RestOperations.class);
        sentraIamJwtDecoder = new SentraIamJwtDecoder(restOperations);
    }

    @Test
    void decode_shouldUseCorrectIssuerAndCacheDecoder() throws Exception {
        String token = "dummy.token.value";

        // Mock JWTParser
        var claimsSet = new JWTClaimsSet.Builder()
                .issuer("https://issuer.example.com")
                .build();

        var jwtMock = mock(com.nimbusds.jwt.JWT.class);
        when(jwtMock.getJWTClaimsSet()).thenReturn(claimsSet);

        // Static mocking for JWTParser.parse
        try (MockedStatic<JWTParser> mockedParser = mockStatic(JWTParser.class)) {
            mockedParser.when(() -> JWTParser.parse(token)).thenReturn(jwtMock);

            // Stub JwtDecoder
            JwtDecoder jwtDecoderMock = mock(JwtDecoder.class);
            Jwt decodedJwtMock = new Jwt(token, Instant.now(), Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"), Map.of("sub", "user"));
            when(jwtDecoderMock.decode(token)).thenReturn(decodedJwtMock);

            // Inject the decoder manually to simulate cache
            sentraIamJwtDecoder = spy(sentraIamJwtDecoder);
            doReturn(jwtDecoderMock).when(sentraIamJwtDecoder).resolveDecoder("https://issuer.example.com");

            Jwt result = sentraIamJwtDecoder.decode(token);

            assertEquals("user", result.getClaim("sub"));
            verify(jwtDecoderMock).decode(token);
        }
    }

    @Test
    void decode_shouldThrowJwtExceptionOnParseError() {
        String invalidToken = "invalid.token";

        try (MockedStatic<JWTParser> mockedParser = mockStatic(JWTParser.class)) {
            mockedParser.when(() -> JWTParser.parse(invalidToken))
                    .thenThrow(new ParseException("Invalid format", 0));

            assertThrows(JwtException.class, () -> sentraIamJwtDecoder.decode(invalidToken));
        }
    }

    @Test
    void decode_shouldUseCorrectIssuerAndUseCacheDecoder() throws Exception {
        String token = "dummy.token.value";

        // Mock JWTParser
        var claimsSet = new JWTClaimsSet.Builder()
                .issuer("https://issuer.example.com")
                .build();

        var jwtMock = mock(com.nimbusds.jwt.JWT.class);
        when(jwtMock.getJWTClaimsSet()).thenReturn(claimsSet);

        // Static mocking for JWTParser.parse
        try (MockedStatic<JWTParser> mockedParser = mockStatic(JWTParser.class)) {
            mockedParser.when(() -> JWTParser.parse(token)).thenReturn(jwtMock);

            // Stub JwtDecoder
            JwtDecoder jwtDecoderMock = mock(JwtDecoder.class);
            Jwt decodedJwtMock = new Jwt(token, Instant.now(), Instant.now().plusSeconds(3600),
                    Map.of("alg", "none"), Map.of("sub", "user"));
            when(jwtDecoderMock.decode(token)).thenReturn(decodedJwtMock);

            // Inject the decoder manually to simulate cache
            sentraIamJwtDecoder = spy(sentraIamJwtDecoder);
            doReturn(jwtDecoderMock).when(sentraIamJwtDecoder).resolveDecoder("https://issuer.example.com");

            sentraIamJwtDecoder.decode(token);
            Jwt result = sentraIamJwtDecoder.decode(token);

            assertEquals("user", result.getClaim("sub"));
            verify(jwtDecoderMock, times(2)).decode(token);
        }
    }
}
