package com.svincent7.sentraiam.common.dto.user;

import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import lombok.Data;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
public class UserResponse implements Authentication {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String tenantId;
    private boolean isAuthenticated;
    private int version;

    public static UserResponse fromClaimsAndPayload(final Claims claims, final JSONObject payload) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(claims.getSubject());
        userResponse.setUsername(payload.getString(TokenConstant.USERNAME));
        userResponse.setFirstName(payload.getString(TokenConstant.FIRSTNAME));
        userResponse.setLastName(payload.getString(TokenConstant.LASTNAME));
        userResponse.setEnabled(true);
        userResponse.setTenantId(claims.getIssuer());
        userResponse.setAuthenticated(true);
        userResponse.setVersion(payload.getInt(TokenConstant.VERSION));
        return userResponse;
    }

    @Override
    public String getName() {
        return firstName + (StringUtils.isEmpty(lastName) ? "" : " " + lastName);
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
        return this.id;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(final boolean authenticated) throws IllegalArgumentException {
        this.isAuthenticated = authenticated;
    }
}
