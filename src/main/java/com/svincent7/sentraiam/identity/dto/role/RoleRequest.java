package com.svincent7.sentraiam.identity.dto.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RoleRequest {
    @NotEmpty
    private String roleName;
    @NotEmpty
    private String tenantId;
}
