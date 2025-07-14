package com.svincent7.sentraiam.identity.dto.role;

import lombok.Data;

@Data
public class RoleResponse {
    private String id;
    private String roleName;
    private String tenantId;
    private int version;
}
