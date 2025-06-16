package com.svincent7.sentraiam.auth.service.token;

public interface TokenGenerator {
    String generateToken(CreateTokenRequest request);
}
