package com.svincent7.sentraiam.auth.service;

import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.auth.LoginResponse;
import com.svincent7.sentraiam.common.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.common.dto.auth.RefreshRequest;

import java.util.List;

public interface AuthenticationService {
    LoginResponse authenticate(LoginRequest loginRequest);
    LoginResponse refresh(RefreshRequest refreshRequest);
    void logout(LogoutRequest logoutRequest);
    List<Object>  getJwks(String tenantId);
}
