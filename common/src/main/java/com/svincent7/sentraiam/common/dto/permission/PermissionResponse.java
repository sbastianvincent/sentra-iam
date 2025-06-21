package com.svincent7.sentraiam.common.dto.permission;

import lombok.Data;

@Data
public class PermissionResponse {
    private String id;
    private String permission;
    private boolean userVisible;
}
