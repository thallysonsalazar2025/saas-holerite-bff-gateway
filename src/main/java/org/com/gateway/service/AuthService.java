package org.com.gateway.service;

import java.util.List;

import org.com.gateway.model.request.LoginRequest;
import org.com.gateway.model.response.LoginResponse;
import org.com.gateway.security.JwtServiceConfig;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtServiceConfig jwtServiceConfig;

    public AuthService(AuthenticationManager authenticationManager, JwtServiceConfig jwtServiceConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtServiceConfig = jwtServiceConfig;
    }

    public LoginResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        List<String> profiles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String token = jwtServiceConfig.generateToken(authentication.getName(), profiles);

        return new LoginResponse(
                token,
                "Bearer",
                jwtServiceConfig.getExpirationInSeconds(),
                profiles.isEmpty() ? "ROLE_USER" : profiles.getFirst()
        );
    }
}
