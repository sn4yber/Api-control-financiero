package com.controfinanciero.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request para login de usuario
 */
public record LoginRequest(
        @NotBlank(message = "Username es requerido")
        String username,

        @NotBlank(message = "Password es requerido")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password
) {}

