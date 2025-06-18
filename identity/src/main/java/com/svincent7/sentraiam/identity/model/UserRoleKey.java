package com.svincent7.sentraiam.identity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
public class UserRoleKey implements Serializable {
    @Serial
    private static final long serialVersionUID = 1198527398177281L;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "role_id")
    private String roleId;

    public UserRoleKey(final String user, final String role) {
        this.userId = user;
        this.roleId = role;
    }
}
