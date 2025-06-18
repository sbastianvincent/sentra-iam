package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.common.auth.token.SentraClaims;
import com.svincent7.sentraiam.common.dto.user.UserResponse;

public interface TokenService {
    AccessToken generateAccessToken(UserResponse userResponse);
    String generateRefreshToken(UserResponse userResponse);
    RefreshToken getResourceByRefreshToken(String refreshToken);
    void expireRefreshToken(String refreshToken);
    SentraClaims authenticate(String accessToken);
}
