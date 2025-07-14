package com.svincent7.sentraiam.identity.service.jwtkey;

import com.svincent7.sentraiam.identity.model.JwtKey;
import com.svincent7.sentraiam.common.service.BaseService;

public abstract class JwtKeyService extends BaseService<JwtKey, JwtKeyRequest, JwtKeyResponse, String> {
    public abstract JwtKeyResponse getTenantActiveJwtKey(String tenantId);
    public abstract JwtKeyResponse generateJwtKey(String tenantId);
}
