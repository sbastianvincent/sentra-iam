package com.svincent7.sentraiam.identity.dto.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserRequest {
    @NotEmpty
    private String username;
    private String firstName;
    private String lastName;
    private boolean enabled;
    @NotEmpty
    private String tenantId;
}
