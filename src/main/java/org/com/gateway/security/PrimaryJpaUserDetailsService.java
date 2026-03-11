package org.com.gateway.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.com.gateway.model.User;
import org.com.gateway.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

@Service
@Primary
@RequiredArgsConstructor
public class PrimaryJpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "ROLE_USER";
        String r = role.trim().toUpperCase(Locale.ROOT);
        return r.startsWith("ROLE_") ? r : "ROLE_" + r;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String role = normalizeRole(u.getRole());
        Set<SimpleGrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority(role));

        return new org.springframework.security.core.userdetails.User(
                u.getUsername(),
                u.getPassword(),
                authorities
        );
    }
}
