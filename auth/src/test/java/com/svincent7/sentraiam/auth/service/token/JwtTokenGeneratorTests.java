package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.common.crypto.CryptoUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

public class JwtTokenGeneratorTests {

    @Test
    void testGenerateToken() {
        KeyPair keyPair = CryptoUtil.generateKeyPair("RSA", 2048);

        CreateTokenRequest createTokenRequest = new CreateTokenRequest();
        createTokenRequest.setSignatureAlgorithm(SignatureAlgorithm.RS256);
        createTokenRequest.setPrivateKey(keyPair.getPrivate());

        JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator();

        String response = jwtTokenGenerator.generateToken(createTokenRequest);

        Assertions.assertNotNull(response);
    }
}
