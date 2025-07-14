package com.svincent7.sentraiam.identity.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModifyRoleRequest {
    @NotEmpty
    private String roleId;
}
