package com.svincent7.sentraiam.identity.model;

import org.junit.jupiter.api.Test;

public class RefreshTokenTests {

    @Test
    void testExpired() {
        long expired = System.currentTimeMillis() - 10000;
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredTimestamp(expired);

        assert refreshToken.isExpired();
    }

    @Test
    void testNotExpired() {
        long expired = System.currentTimeMillis() + 10000;
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiredTimestamp(expired);

        assert !refreshToken.isExpired();
    }
}
