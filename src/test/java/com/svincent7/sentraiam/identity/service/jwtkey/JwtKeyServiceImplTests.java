package com.svincent7.sentraiam.identity.service.jwtkey;

import com.svincent7.sentraiam.identity.config.IdentityConfig;
import com.svincent7.sentraiam.identity.model.JwtKey;
import com.svincent7.sentraiam.identity.repository.JwtKeyRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

public class JwtKeyServiceImplTests {
    private IdentityConfig config;
    private JwtKeyRepository jwtKeyRepository;
    private JwtKeyMapper jwtKeyMapper;
    private JwtKeyServiceImpl jwtKeyService;

    @BeforeEach
    void setup() {
        config = Mockito.mock(IdentityConfig.class);
        jwtKeyRepository = Mockito.mock(JwtKeyRepository.class);
        jwtKeyMapper = Mockito.mock(JwtKeyMapper.class);
        jwtKeyService = new JwtKeyServiceImpl(config, jwtKeyRepository, jwtKeyMapper);
    }

    @Test
    void testGetJpaRepository() {
        Assertions.assertEquals(jwtKeyRepository, jwtKeyService.getRepository());
    }

    @Test
    void testGetMapper() {
        Assertions.assertEquals(jwtKeyMapper, jwtKeyService.getMapper());
    }

    @Test
    void testGetTenantActiveJwtKey() {
        String tenantId = "test-tenant";

        JwtKey jwtKey = Mockito.mock(JwtKey.class);
        Mockito.when(jwtKey.isExpired()).thenReturn(false);

        Mockito.when(jwtKeyRepository.findFirstByTenantIdOrderByKeyVersionDesc("test-tenant")).thenReturn(Optional.of(jwtKey));

        JwtKeyResponse response = Mockito.mock(JwtKeyResponse.class);
        Mockito.when(jwtKeyMapper.toResponseDTO(jwtKey)).thenReturn(response);

        Assertions.assertEquals(jwtKeyService.getTenantActiveJwtKey(tenantId), response);
    }

    @Test
    void testGetTenantActiveJwtKey_Expired() {
        String tenantId = "test-tenant";

        JwtKey jwtKey = Mockito.mock(JwtKey.class);
        Mockito.when(jwtKey.isExpired()).thenReturn(true);

        Mockito.when(jwtKeyRepository.findFirstByTenantIdOrderByKeyVersionDesc("test-tenant")).thenReturn(Optional.of(jwtKey));
        Mockito.when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");
        Mockito.when(config.getJwtDefaultKeyLength()).thenReturn(2048);
        Mockito.when(jwtKeyMapper.toEntity(Mockito.any())).thenReturn(Mockito.mock(JwtKey.class));
        Mockito.when(jwtKeyRepository.save(Mockito.any(JwtKey.class))).thenReturn(Mockito.mock(JwtKey.class));

        JwtKeyResponse response = Mockito.mock(JwtKeyResponse.class);
        Mockito.when(jwtKeyMapper.toResponseDTO(Mockito.any())).thenReturn(response);

        JwtKeyResponse resp = jwtKeyService.getTenantActiveJwtKey(tenantId);

        Assertions.assertEquals(resp, response);
    }

    @Test
    void testGetTenantActiveJwtKey_NotPresent() {
        String tenantId = "test-tenant";

        Mockito.when(jwtKeyRepository.findFirstByTenantIdOrderByKeyVersionDesc("test-tenant")).thenReturn(Optional.empty());
        Mockito.when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");
        Mockito.when(config.getJwtDefaultKeyLength()).thenReturn(2048);
        Mockito.when(jwtKeyMapper.toEntity(Mockito.any())).thenReturn(Mockito.mock(JwtKey.class));
        Mockito.when(jwtKeyRepository.save(Mockito.any(JwtKey.class))).thenReturn(Mockito.mock(JwtKey.class));

        JwtKeyResponse response = Mockito.mock(JwtKeyResponse.class);
        Mockito.when(jwtKeyMapper.toResponseDTO(Mockito.any())).thenReturn(response);

        JwtKeyResponse resp = jwtKeyService.getTenantActiveJwtKey(tenantId);

        Assertions.assertEquals(resp, response);
    }

    @Test
    void testGenerateJwtKey() {
        String tenantId = "test-tenant";

        Mockito.when(jwtKeyRepository.findFirstByTenantIdOrderByKeyVersionDesc("test-tenant")).thenReturn(Optional.empty());
        Mockito.when(config.getJwtDefaultKeyPairAlgorithm()).thenReturn("RSA");
        Mockito.when(config.getJwtDefaultKeyLength()).thenReturn(2048);
        Mockito.when(jwtKeyMapper.toEntity(Mockito.any())).thenReturn(Mockito.mock(JwtKey.class));
        Mockito.when(jwtKeyRepository.save(Mockito.any(JwtKey.class))).thenReturn(Mockito.mock(JwtKey.class));

        JwtKeyResponse response = Mockito.mock(JwtKeyResponse.class);
        Mockito.when(jwtKeyMapper.toResponseDTO(Mockito.any())).thenReturn(response);

        JwtKeyResponse resp = jwtKeyService.generateJwtKey(tenantId);

        Assertions.assertEquals(resp, response);
    }

}