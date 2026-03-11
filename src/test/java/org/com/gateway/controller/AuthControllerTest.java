package org.com.gateway.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.com.gateway.service.AuthService;
import org.com.gateway.model.request.LoginRequest;
import org.com.gateway.model.response.LoginResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("invalidUser", "wrongPassword");
        LoginResponse loginResponse = new LoginResponse("testToken", "Bearer", 3600L, "ROLE_USER");

        when(authService.authenticate(loginRequest)).thenReturn(loginResponse);

        // Act
        ResponseEntity<LoginResponse> responseEntity = authController.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loginResponse, responseEntity.getBody());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("invalidUser", "wrongPassword");

        when(authService.authenticate(loginRequest)).thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authController.login(loginRequest));
    }
}