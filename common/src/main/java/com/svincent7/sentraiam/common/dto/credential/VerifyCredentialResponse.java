package com.svincent7.sentraiam.common.dto.credential;

import com.svincent7.sentraiam.common.dto.user.UserResponse;
import lombok.Data;

@Data
public class VerifyCredentialResponse {
    private VerifyCredentialStatus status;
    private UserResponse user;
}
