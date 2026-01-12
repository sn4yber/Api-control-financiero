package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoCategoria;

import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Categoría.
 */
public record CategoriaDTO(
        Long id,
        Long usuarioId,
        String nombre,
        String descripcion,
        String colorHex,
        String icono,
        TipoCategoria tipoCategoria,
        boolean activa,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

