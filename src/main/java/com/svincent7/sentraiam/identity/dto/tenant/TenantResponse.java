package com.svincent7.sentraiam.identity.dto.tenant;

import lombok.Data;

@Data
public class TenantResponse {
    private String id;
    private String tenantName;
    private TenantStatus tenantStatus;
    private int version;
}
