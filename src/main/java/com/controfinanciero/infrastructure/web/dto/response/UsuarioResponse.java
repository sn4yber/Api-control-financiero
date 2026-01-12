package com.controfinanciero.infrastructure.web.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Usuario.
 */
public record UsuarioResponse(
        Long id,
        String username,
        String email,
        String fullName,
        boolean active,
        LocalDateTime createdAt
) {
}

