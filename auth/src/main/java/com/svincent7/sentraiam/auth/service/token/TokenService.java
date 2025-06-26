package com.svincent7.sentraiam.auth.service.token;

import com.svincent7.sentraiam.auth.dto.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.auth.model.AccessToken;
import com.svincent7.sentraiam.auth.model.RefreshToken;
import com.svincent7.sentraiam.common.dto.user.UserResponse;

public interface TokenService {
    AccessToken generateAccessToken(GenerateAccessTokenRequest request);
    String generateRefreshToken(UserResponse userResponse);
    RefreshToken getResourceByRefreshToken(String refreshToken);
    void expireRefreshToken(String refreshToken);
}
