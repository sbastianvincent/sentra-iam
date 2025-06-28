package com.svincent7.sentraiam.apigateway.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.mock;

class SentraIamJwtDecoderTests {

    private WebClient webClient;
    private SentraIamJwtDecoder sentraIamJwtDecoder;

    @BeforeEach
    void setUp() {
        webClient = mock(WebClient.class);
        sentraIamJwtDecoder = new SentraIamJwtDecoder(webClient);    }

    @Test
    void testDecode_ValidToken_ShouldReturnJwt() {
        String token = "abc";
        sentraIamJwtDecoder.decode(token);
    }
}
