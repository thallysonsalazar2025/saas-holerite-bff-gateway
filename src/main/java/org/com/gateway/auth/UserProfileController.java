package org.com.gateway.auth;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    /**
     * Retorna o perfil do usuário autenticado.
     * Endpoint descritivo: /api/users/profile
     */
    @GetMapping("/profile")
    public Map<String, Object> getUserProfile(Authentication authentication) {
        return Map.of(
                "username", authentication.getName(),
                "roles", authentication.getAuthorities()
        );
    }
}