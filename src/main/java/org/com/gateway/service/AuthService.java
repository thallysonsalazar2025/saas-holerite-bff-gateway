package org.com.gateway.service;

import org.com.gateway.model.request.LoginRequest;
import org.com.gateway.model.response.LoginResponse;
import org.com.gateway.security.JwtServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final AuthenticationManager authenticationManager;
    private final JwtServiceConfig jwtServiceConfig;

    public AuthService(AuthenticationManager authenticationManager, JwtServiceConfig jwtServiceConfig) {
        this.authenticationManager = authenticationManager;
        this.jwtServiceConfig = jwtServiceConfig;
    }

    public LoginResponse authenticate(LoginRequest request) {
        log.info("Autenticando usuário: {}", request.username());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        log.info("Usuário autenticado com sucesso: {}", authentication.getName());

        List<String> profiles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        log.debug("Perfis do usuário {}: {}", authentication.getName(), profiles);

        String token = jwtServiceConfig.generateToken(authentication.getName(), profiles);
        log.info("Token JWT gerado para o usuário: {}", authentication.getName());

        LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                jwtServiceConfig.getExpirationInSeconds(),
                profiles.isEmpty() ? "ROLE_USER" : profiles.getFirst()
        );

        log.info("Resposta de login criada para o usuário: {}", authentication.getName());
        return response;
    }
}
