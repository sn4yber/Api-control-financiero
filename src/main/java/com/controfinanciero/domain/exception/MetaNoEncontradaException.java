package com.controfinanciero.domain.exception;

/**
 * Excepción lanzada cuando se intenta acceder a una meta financiera que no existe.
 */
public class MetaNoEncontradaException extends DomainException {

    public MetaNoEncontradaException(Long metaId) {
        super("Meta financiera no encontrada con ID: " + metaId);
    }

    public MetaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

