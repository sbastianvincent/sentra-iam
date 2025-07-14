package com.svincent7.sentraiam.identity.dto.credential;

import com.svincent7.sentraiam.identity.dto.user.UserResponse;
import lombok.Data;

@Data
public class VerifyCredentialResponse {
    private VerifyCredentialStatus status;
    private UserResponse user;
}
