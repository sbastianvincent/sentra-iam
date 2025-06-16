package com.svincent7.sentraiam.auth.service.jwtkey;

import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.common.service.BaseService;

public abstract class JwtKeyService extends BaseService<JwtKey, JwtKeyRequest, JwtKeyResponse, String> {
    public abstract JwtKeyResponse getTenantActiveJwtKey(String tenantId);
    public abstract JwtKeyResponse generateJwtKey(String tenantId);
}
