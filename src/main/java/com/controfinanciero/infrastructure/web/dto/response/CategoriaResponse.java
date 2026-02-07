package com.controfinanciero.infrastructure.web.dto.response;

import com.controfinanciero.domain.model.enums.TipoCategoria;

import java.time.LocalDateTime;

public record CategoriaResponse(
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
