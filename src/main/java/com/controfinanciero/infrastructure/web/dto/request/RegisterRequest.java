package com.controfinanciero.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request para registro de usuario
 */
public record RegisterRequest(
        @NotBlank(message = "Username es requerido")
        @Size(min = 3, max = 50, message = "Username debe tener entre 3 y 50 caracteres")
        String username,

        @NotBlank(message = "Email es requerido")
        @Email(message = "Email debe ser válido")
        String email,

        @NotBlank(message = "Password es requerido")
        @Size(min = 6, message = "Password debe tener al menos 6 caracteres")
        String password,

        String fullName
) {}

