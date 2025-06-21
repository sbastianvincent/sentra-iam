package com.svincent7.sentraiam.common.dto.role;

import lombok.Data;

@Data
public class RoleResponse {
    private String id;
    private String roleName;
    private String tenantId;
    private int version;
}
