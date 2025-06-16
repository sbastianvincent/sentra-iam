package com.svincent7.sentraiam.auth.service.jwtkey;

import com.svincent7.sentraiam.auth.model.JwtKey;
import com.svincent7.sentraiam.auth.model.KeyAlgorithm;
import com.svincent7.sentraiam.common.service.BaseMapper;
import lombok.Getter;
import org.mapstruct.Mapper;

@Getter
@Mapper(componentModel = "spring")
public abstract class JwtKeyMapper implements BaseMapper<JwtKey, JwtKeyRequest, JwtKeyResponse> {

    @Override
    public void updateEntityFromDTO(final JwtKeyRequest request, final JwtKey entity) {
        entity.setKeyVersion(request.getKeyVersion());
        entity.setKeyValue(request.getKeyValue());
        entity.setKeyAlgorithm(KeyAlgorithm.valueOf(request.getKeyAlgorithm()));
        entity.setExpiredTimestamp(request.getExpiredTimestamp());
    }

    @Override
    public JwtKey toEntity(final JwtKeyRequest request) {
        if (request == null) {
            return null;
        }

        JwtKey jwtKey = new JwtKey();
        jwtKey.setTenantId(request.getTenantId());
        updateEntityFromDTO(request, jwtKey);

        return jwtKey;
    }
}
