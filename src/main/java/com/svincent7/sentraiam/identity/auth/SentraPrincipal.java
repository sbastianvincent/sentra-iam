package com.svincent7.sentraiam.identity.auth;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Collection;

@ToString
@Getter
public class SentraPrincipal implements Authentication {
    @Serial
    private static final long serialVersionUID = 1129838666311828L;

    private final String tenantId;
    private final String userId;
    private final String name;
    private boolean authenticated;
    private final Collection<? extends GrantedAuthority> authorities;

    public SentraPrincipal(final String tenantIdInput, final String userIdInput, final String usernameInput,
                           final boolean authenticatedInput,
                           final Collection<? extends GrantedAuthority> authoritiesInput) {
        this.tenantId = tenantIdInput;
        this.userId = userIdInput;
        this.name = usernameInput;
        this.authenticated = authenticatedInput;
        this.authorities = authoritiesInput;
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
