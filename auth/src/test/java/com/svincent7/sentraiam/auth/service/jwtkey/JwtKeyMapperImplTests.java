package com.svincent7.sentraiam.auth.service.jwtkey;

import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.auth.model.KeyAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtKeyMapperImplTests {
    private JwtKeyMapperImpl keyMapper;

    @BeforeEach
    void setup() {
        keyMapper = new JwtKeyMapperImpl();
    }

    @Test
    void testToResponseDto() {
        JwtKey jwtKey = new JwtKey();
        jwtKey.setId("id");
        jwtKey.setTenantId("test-tenant");
        jwtKey.setKeyVersion("1");
        jwtKey.setPrivateKey("privateKey");
        jwtKey.setPublicKey("publicKey");
        jwtKey.setKeyAlgorithm(KeyAlgorithm.ES256);
        jwtKey.setCreatedTimestamp(System.currentTimeMillis());
        jwtKey.setExpiredTimestamp(System.currentTimeMillis() + 10000);

        JwtKeyResponse response = keyMapper.toResponseDTO(jwtKey);

        assert response != null;
        Assertions.assertEquals(jwtKey.getId(), response.getId());
        Assertions.assertEquals(jwtKey.getTenantId(), response.getTenantId());
        Assertions.assertEquals(jwtKey.getKeyVersion(), response.getKeyVersion());
        Assertions.assertEquals(jwtKey.getPrivateKey(), response.getPrivateKey());
        Assertions.assertEquals(jwtKey.getPublicKey(), response.getPublicKey());
        Assertions.assertEquals(jwtKey.getKeyAlgorithm(), response.getKeyAlgorithm());
        Assertions.assertEquals(jwtKey.getCreatedTimestamp(), response.getCreatedTimestamp());
        Assertions.assertEquals(jwtKey.getExpiredTimestamp(), response.getExpiredTimestamp());
    }

    @Test
    void testToResponseDto_Null() {
        Assertions.assertNull(keyMapper.toResponseDTO(null));
    }

    @Test
    void testToResponseDto_CreatedNull() {
        JwtKey jwtKey = new JwtKey();
        jwtKey.setId("id");
        jwtKey.setTenantId("test-tenant");
        jwtKey.setKeyVersion("1");
        jwtKey.setPrivateKey("privateKey");
        jwtKey.setPublicKey("publicKey");
        jwtKey.setKeyAlgorithm(KeyAlgorithm.ES256);
        jwtKey.setCreatedTimestamp(null);
        jwtKey.setExpiredTimestamp(System.currentTimeMillis() + 10000);

        JwtKeyResponse response = keyMapper.toResponseDTO(jwtKey);

        assert response != null;
        Assertions.assertEquals(jwtKey.getId(), response.getId());
        Assertions.assertEquals(jwtKey.getTenantId(), response.getTenantId());
        Assertions.assertEquals(jwtKey.getKeyVersion(), response.getKeyVersion());
        Assertions.assertEquals(jwtKey.getPrivateKey(), response.getPrivateKey());
        Assertions.assertEquals(jwtKey.getPublicKey(), response.getPublicKey());
        Assertions.assertEquals(jwtKey.getKeyAlgorithm(), response.getKeyAlgorithm());
        Assertions.assertEquals(jwtKey.getExpiredTimestamp(), response.getExpiredTimestamp());
        Assertions.assertNotNull(response.getExpiredTimestamp());
    }
}
