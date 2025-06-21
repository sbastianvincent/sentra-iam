package com.svincent7.sentraiam.identity.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@Data
@Entity
@Table(name = "user_roles")
@NoArgsConstructor
public class UserRoleMapping implements Serializable {

    @EmbeddedId
    private UserRoleKey userRoleKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RoleEntity role;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @Column(name = "role_id", insertable = false, updatable = false)
    private String roleId;

    public UserRoleMapping(final UserEntity userInput, final RoleEntity roleInput) {
        this.user = userInput;
        this.role = roleInput;
        this.userRoleKey = new UserRoleKey(user.getId(), role.getId());
        this.userId = user.getId();
        this.roleId = role.getId();
    }
}
