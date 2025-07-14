package com.svincent7.sentraiam.identity.dto.tenant;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TenantRequest {
    @NotEmpty
    private String tenantName;
    @NotNull
    private TenantStatus tenantStatus;
}
