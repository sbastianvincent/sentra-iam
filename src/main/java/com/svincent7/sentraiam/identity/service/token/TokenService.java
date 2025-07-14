package com.svincent7.sentraiam.identity.service.token;

import com.svincent7.sentraiam.identity.dto.credential.GenerateAccessTokenRequest;
import com.svincent7.sentraiam.identity.model.AccessToken;
import com.svincent7.sentraiam.identity.model.RefreshToken;
import com.svincent7.sentraiam.identity.dto.user.UserResponse;

public interface TokenService {
    AccessToken generateAccessToken(GenerateAccessTokenRequest request);
    String generateRefreshToken(UserResponse userResponse);
    RefreshToken getResourceByRefreshToken(String refreshToken);
    void expireRefreshToken(String refreshToken);
}
