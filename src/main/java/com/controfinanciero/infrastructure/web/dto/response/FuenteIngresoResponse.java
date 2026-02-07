package com.controfinanciero.infrastructure.web.dto.response;

import com.controfinanciero.domain.model.enums.TipoFuente;
import java.time.LocalDateTime;

/**
 * Response para fuente de ingreso
 */
public record FuenteIngresoResponse(
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

