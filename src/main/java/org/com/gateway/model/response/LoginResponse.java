package org.com.gateway.model.response;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String profile
) {
}
