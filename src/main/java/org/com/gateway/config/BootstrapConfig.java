package org.com.gateway.config;

import lombok.RequiredArgsConstructor;
import org.com.gateway.model.User;
import org.com.gateway.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BootstrapConfig {

    private final UserRepository userRepository;

    @Bean
    public CommandLineRunner initAdmin() {
        return args -> {
            String adminUser = "admin";
            if (userRepository.findByUsername(adminUser).isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                User user = new User();
                user.setUsername(adminUser);
                user.setPassword(encoder.encode("admin123"));
                user.setRole("ROLE_ADMIN");
                userRepository.save(user);
                System.out.println("Bootstrap admin created: username=admin password=admin123 role=ROLE_ADMIN");
            }
        };
    }
}
