package com.controfinanciero.domain.exception;

/**
 * Excepción lanzada cuando se intenta acceder a una categoría que no existe.
 */
public class CategoriaNoEncontradaException extends DomainException {

    public CategoriaNoEncontradaException(Long categoriaId) {
        super("Categoría no encontrada con ID: " + categoriaId);
    }

    public CategoriaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}

