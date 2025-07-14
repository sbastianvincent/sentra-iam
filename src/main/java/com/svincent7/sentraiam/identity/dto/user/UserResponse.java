package com.svincent7.sentraiam.identity.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private String tenantId;
    private int version;
}
