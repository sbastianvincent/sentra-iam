package com.svincent7.sentraiam.identity.service.jwtkey;

import com.svincent7.sentraiam.identity.model.JwtKey;
import com.svincent7.sentraiam.identity.model.KeyAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtKeyMapperTests {
    private JwtKeyMapper keyMapper;

    @BeforeEach
    void setup() {
        keyMapper = new JwtKeyMapperImpl();
    }

    @Test
    void testUpdateEntityFromDto() {
        JwtKeyRequest request = new JwtKeyRequest();
        request.setKeyVersion("1");
        request.setPrivateKey("privateKey");
        request.setPublicKey("publicKey");
        request.setKeyAlgorithm(KeyAlgorithm.ES256);
        request.setKeyLength(256);
        request.setExpiredTimestamp(System.currentTimeMillis() + 10000);
        request.setTenantId("test-tenant");

        JwtKey entity = new JwtKey();
        entity.setTenantId("tenant");

        keyMapper.updateEntityFromDTO(request, entity);

        Assertions.assertEquals(request.getKeyVersion(), entity.getKeyVersion());
        Assertions.assertEquals(request.getPrivateKey(), entity.getPrivateKey());
        Assertions.assertEquals(request.getPublicKey(), entity.getPublicKey());
        Assertions.assertEquals(request.getKeyAlgorithm(), entity.getKeyAlgorithm());
        Assertions.assertEquals(request.getKeyLength(), entity.getKeyLength());
        Assertions.assertEquals(request.getExpiredTimestamp(), entity.getExpiredTimestamp());
        Assertions.assertNotEquals(request.getTenantId(), entity.getTenantId());
        Assertions.assertEquals("tenant", entity.getTenantId());
    }

    @Test
    void testToEntity() {
        JwtKeyRequest request = new JwtKeyRequest();
        request.setKeyVersion("1");
        request.setPrivateKey("privateKey");
        request.setPublicKey("publicKey");
        request.setKeyAlgorithm(KeyAlgorithm.ES256);
        request.setKeyLength(256);
        request.setExpiredTimestamp(System.currentTimeMillis() + 10000);
        request.setTenantId("test-tenant");

        JwtKey entity = keyMapper.toEntity(request);

        Assertions.assertNotNull(entity);
        Assertions.assertEquals(request.getTenantId(), entity.getTenantId());
        Assertions.assertEquals(request.getKeyVersion(), entity.getKeyVersion());
        Assertions.assertEquals(request.getPrivateKey(), entity.getPrivateKey());
        Assertions.assertEquals(request.getPublicKey(), entity.getPublicKey());
        Assertions.assertEquals(request.getKeyAlgorithm(), entity.getKeyAlgorithm());
        Assertions.assertEquals(request.getKeyLength(), entity.getKeyLength());
        Assertions.assertEquals(request.getExpiredTimestamp(), entity.getExpiredTimestamp());
    }

    @Test
    void testToEntityNull() {
        Assertions.assertNull(keyMapper.toEntity(null));
    }
}
