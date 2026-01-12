package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoFuente;
import java.time.LocalDateTime;

/**
 * DTO para representar una fuente de ingreso
 */
public record FuenteIngresoDTO(
        Long id,
        Long usuarioId,
        String nombre,
        String descripcion,
        TipoFuente tipoFuente,
        Boolean esIngresoReal,
        Boolean activa,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

