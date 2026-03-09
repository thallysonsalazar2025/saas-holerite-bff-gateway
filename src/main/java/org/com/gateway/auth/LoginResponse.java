package org.com.gateway.auth;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String profile
) {
}
