package com.controfinanciero.application.dto;

/**
 * DTO para crear un usuario.
 */
public record CrearUsuarioCommand(
        String username,
        String email,
        String password,
        String fullName
) {
}
