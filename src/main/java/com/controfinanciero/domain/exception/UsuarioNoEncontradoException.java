package com.controfinanciero.domain.exception;

/**
 * Excepción lanzada cuando se intenta acceder a un usuario que no existe.
 */
public class UsuarioNoEncontradoException extends DomainException {

    public UsuarioNoEncontradoException(Long usuarioId) {
        super("Usuario no encontrado con ID: " + usuarioId);
    }

    public UsuarioNoEncontradoException(String email) {
        super("Usuario no encontrado con email: " + email);
    }
}

