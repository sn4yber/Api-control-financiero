package com.controfinanciero.application.dto;

import java.time.LocalDateTime;

/**
 * DTO de respuesta para Usuario.
 */
public record UsuarioDTO(
        Long id,
        String username,
        String email,
        String fullName,
        boolean active,
        LocalDateTime createdAt
) {
}

