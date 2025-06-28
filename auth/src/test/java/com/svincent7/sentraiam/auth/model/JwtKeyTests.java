package com.svincent7.sentraiam.auth.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwtKeyTests {

    @Test
    void testExpired() {
        JwtKey jwtKey = new JwtKey();
        jwtKey.setExpiredTimestamp(System.currentTimeMillis() - 10000);

        Assertions.assertTrue(jwtKey.isExpired());
    }

    @Test
    void testNotExpired() {
        JwtKey jwtKey = new JwtKey();
        jwtKey.setExpiredTimestamp(System.currentTimeMillis() + 10000);

        Assertions.assertFalse(jwtKey.isExpired());
    }

    @Test
    void testToString() {
        long time = System.currentTimeMillis() + 10000;
        String id = "id";
        String tenantId = "test-tenant";
        KeyAlgorithm keyAlgorithm = KeyAlgorithm.RS256;
        String keyVersion = "1";
        String privateKey = "privateKey";
        String publicKey = "publicKey";

        JwtKey jwtKey = new JwtKey();
        jwtKey.setId(id);
        jwtKey.setTenantId(tenantId);
        jwtKey.setKeyAlgorithm(keyAlgorithm);
        jwtKey.setKeyVersion(keyVersion);
        jwtKey.setPrivateKey(privateKey);
        jwtKey.setPublicKey(publicKey);
        jwtKey.setCreatedTimestamp(time);
        jwtKey.setExpiredTimestamp(time);

        Assertions.assertEquals("JwtKey{id='id', tenantId='test-tenant', keyVersion='1', privateKey=[PROTECTED]', publicKey='publicKey', keyAlgorithm=RS256, createdTimestamp="+ time +", expiredTimestamp="+ time +"}", jwtKey.toString());
    }
}
