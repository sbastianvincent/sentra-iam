package com.svincent7.sentraiam.common.permission;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Permission {
    TOKEN_INTROSPECT("token:introspect", false),

    PERMISSIONS_GET_ALL("permissions:get-all", false),
    PERMISSIONS_LIST("permissions:list", true),

    ROLES_LIST("roles:list", true),
    ROLES_GET("roles:get", true),
    ROLES_CREATE("roles:create", true),
    ROLES_UPDATE("roles:update", true),
    ROLES_DELETE("roles:delete", true),

    TENANTS_LIST("tenants:list", false),
    TENANTS_GET("tenants:get", false),
    TENANTS_CREATE("tenants:create", false),
    TENANTS_UPDATE("tenants:update", false),

    USERS_LIST("users:list", true),
    USERS_GET("users:get", true),
    USERS_CREATE("users:create", true),
    USERS_UPDATE("users:update", true),
    USERS_DELETE("users:delete", true),

    CREDENTIALS_VERIFY("credentials:verify", false),
    CREDENTIALS_GET("credentials:get", true),
    CREDENTIALS_CREATE("credentials:create", true),
    CREDENTIALS_UPDATE("credentials:update", true),
    CREDENTIALS_DELETE("credentials:delete", true);

    private final String permission;
    private final boolean userVisible;
    Permission(final String permissionInput, final boolean userVisibleInput) {
        this.permission = permissionInput;
        this.userVisible = userVisibleInput;
    }

    public static final Map<String, Permission> PERMISSIONS = new HashMap<>();

    static {
        for (Permission t : Permission.values()) {
            PERMISSIONS.put(t.getPermission(), t);
        }
    }
}
