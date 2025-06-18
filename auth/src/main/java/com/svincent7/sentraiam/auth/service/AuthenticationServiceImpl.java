package com.svincent7.sentraiam.auth.service;

import com.svincent7.sentraiam.auth.client.SentraIamIdentityClient;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.auth.service.token.TokenService;
import com.svincent7.sentraiam.common.auth.token.AuthTokenProvider;
import com.svincent7.sentraiam.common.dto.auth.LoginRequest;
import com.svincent7.sentraiam.common.dto.auth.LoginResponse;
import com.svincent7.sentraiam.common.dto.auth.LogoutRequest;
import com.svincent7.sentraiam.common.dto.auth.RefreshRequest;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialResponse;
import com.svincent7.sentraiam.common.dto.credential.VerifyCredentialStatus;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import com.svincent7.sentraiam.common.exception.AuthenticationException;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthTokenProvider authTokenProvider;
    private final SentraIamIdentityClient sentraIamIdentityClient;
    private final TokenService tokenService;

    @Override
    public LoginResponse authenticate(final LoginRequest loginRequest) {
        if (StringUtils.isEmpty(loginRequest.getTenantId()) && StringUtils.isEmpty(loginRequest.getTenantName())) {
            throw new AuthenticationException("Tenant id or Tenant name are required");
        }

        ResponseEntity<VerifyCredentialResponse> response = sentraIamIdentityClient.verifyCredentials(
                authTokenProvider.getProviderAuthToken(), loginRequest);
        VerifyCredentialResponse credentialResponse = response.getBody();
        if (credentialResponse == null || !credentialResponse.getStatus().equals(VerifyCredentialStatus.SUCCESS)
                || credentialResponse.getUser() == null) {
            log.info("Credential Response: {}", credentialResponse);
            throw new AuthenticationException("Invalid credentials");
        }

        return generateLoginResponse(credentialResponse.getUser());
    }

    @Override
    public LoginResponse refresh(final RefreshRequest request) {
        RefreshToken refreshToken = tokenService.getResourceByRefreshToken(request.getRefreshToken());
        ResponseEntity<UserResponse> response = sentraIamIdentityClient.getUser(
                authTokenProvider.getProviderAuthToken(), refreshToken.getUserId());
        UserResponse user = response.getBody();

        if (user == null) {
            throw new UnauthorizedException("Invalid refresh token");
        }

        if (!user.isEnabled()) {
            throw new UnauthorizedException("Identity is not active. Please contact Administrator");
        }
        return generateLoginResponse(user);
    }

    @Override
    public void logout(final LogoutRequest logoutRequest) {
        tokenService.expireRefreshToken(logoutRequest.getRefreshToken());
    }

    private LoginResponse generateLoginResponse(final UserResponse userResponse) {
        AccessToken accessToken = tokenService.generateAccessToken(userResponse);
        String refreshToken = tokenService.generateRefreshToken(userResponse);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken.getAccessToken());
        loginResponse.setUserId(userResponse.getId());
        loginResponse.setTenantId(userResponse.getTenantId());
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setExpiresAt(accessToken.getExpiredAt());
        return loginResponse;
    }
}
