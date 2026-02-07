package com.controfinanciero.infrastructure.web.dto.response;

/**
 * Response de autenticación con token JWT
 */
public record AuthResponse(
        String token,
        String type,
        Long userId,
        String username,
        String email,
        String fullName
) {
    public static AuthResponse of(String token, Long userId, String username, String email, String fullName) {
        return new AuthResponse(token, "Bearer", userId, username, email, fullName);
    }
}

