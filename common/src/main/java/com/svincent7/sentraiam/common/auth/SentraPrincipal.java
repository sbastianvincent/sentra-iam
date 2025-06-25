package com.svincent7.sentraiam.common.auth;

import com.svincent7.sentraiam.common.auth.token.TokenUtils;
import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.ToString;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;
import java.util.List;

@ToString
@Getter
public class SentraPrincipal implements Authentication {
    @Serial
    private static final long serialVersionUID = 1129838666311828L;

    private final String tenantId;
    private final String userId;
    private final String username;
    private boolean authenticated;

    public SentraPrincipal(final String tenantIdInput, final String userIdInput, final String usernameInput,
                           final boolean authenticatedInput) {
        this.tenantId = tenantIdInput;
        this.userId = userIdInput;
        this.username = usernameInput;
        this.authenticated = authenticatedInput;
    }

    public static SentraPrincipal fromToken(final String token) {
        JSONObject payload = TokenUtils.parsePayloadFromToken(token);
        String username = payload.getString(TokenConstant.USERNAME);
        String userId = payload.getString(Claims.SUBJECT);
        String tenantId = payload.getString(TokenConstant.TENANT_ID);
        return new SentraPrincipal(tenantId, userId, username, true);
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userId;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}
