package com.svincent7.sentraiam.common.dto.role;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModifyPermissionRequest {
    @NotEmpty
    private String permissionId;
}
