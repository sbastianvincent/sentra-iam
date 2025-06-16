package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.common.dto.user.UserResponse;
import org.springframework.security.core.Authentication;

public interface TokenService {
    AccessToken generateAccessToken(UserResponse userResponse);
    String generateRefreshToken(UserResponse userResponse);
    RefreshToken getResourceByRefreshToken(String refreshToken);
    void expireRefreshToken(String refreshToken);
    Authentication authenticate(String accessToken);
    void claimAccessToken(String accessToken);
}
