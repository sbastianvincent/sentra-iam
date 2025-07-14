package com.svincent7.sentraiam.identity.dto.permission;

import lombok.Data;

@Data
public class PermissionResponse {
    private String id;
    private String permission;
    private boolean userVisible;
}
