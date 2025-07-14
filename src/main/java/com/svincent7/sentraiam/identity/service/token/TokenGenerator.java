package com.svincent7.sentraiam.identity.service.token;

public interface TokenGenerator {
    String generateToken(CreateTokenRequest request);
}
