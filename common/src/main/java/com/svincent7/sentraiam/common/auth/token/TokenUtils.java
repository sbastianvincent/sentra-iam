package com.svincent7.sentraiam.common.auth.token;

import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import org.json.JSONObject;

import java.time.Instant;
import java.util.Base64;

public final class TokenUtils {
    private TokenUtils() {

    }

    public static JSONObject parsePayloadFromToken(final String token) throws UnauthorizedException {
        String[] parts = token.split("\\.");
        if (parts.length != TokenConstant.TOTAL_TOKEN_INDEX) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        JSONObject payload = new JSONObject(decode(parts[TokenConstant.TOKEN_PAYLOAD_INDEX]));
        int expiration = payload.getInt(Claims.EXPIRATION);
        String tenantId = payload.getString(Claims.ISSUER);

        if (tenantId == null || expiration == 0) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        if (Instant.ofEpochSecond(expiration).isBefore(Instant.now())) {
            throw new UnauthorizedException("Token is expired");
        }

        return payload;
    }

    public static JSONObject parseHeaderFromToken(final String token) throws UnauthorizedException {
        String[] parts = token.split("\\.");
        if (parts.length != TokenConstant.TOTAL_TOKEN_INDEX) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        JSONObject payload = new JSONObject(decode(parts[TokenConstant.TOKEN_HEADER_INDEX]));
        String keyId = payload.getString(TokenConstant.KEY_ID);

        if (StringUtils.isEmpty(keyId)) {
            throw new UnauthorizedException("Invalid Access Token");
        }

        return payload;
    }

    private static String decode(final String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
