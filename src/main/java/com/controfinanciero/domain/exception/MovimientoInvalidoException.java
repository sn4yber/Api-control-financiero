package com.controfinanciero.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una operación inválida con un movimiento financiero.
 */
public class MovimientoInvalidoException extends DomainException {

    public MovimientoInvalidoException(String message) {
        super(message);
    }

    public MovimientoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public static MovimientoInvalidoException montoInvalido(String detalle) {
        return new MovimientoInvalidoException("Monto inválido: " + detalle);
    }

    public static MovimientoInvalidoException tipoInvalido(String detalle) {
        return new MovimientoInvalidoException("Tipo de movimiento inválido: " + detalle);
    }

    public static MovimientoInvalidoException fechaInvalida(String detalle) {
        return new MovimientoInvalidoException("Fecha inválida: " + detalle);
    }
}
