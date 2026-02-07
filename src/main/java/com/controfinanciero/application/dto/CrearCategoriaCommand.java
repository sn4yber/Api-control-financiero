package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoCategoria;

/**
 * Command para crear una categoría.
 */
public record CrearCategoriaCommand(
        Long usuarioId,
        String nombre,
        String descripcion,
        TipoCategoria tipoCategoria,
        String colorHex,
        String icono
) {
}

