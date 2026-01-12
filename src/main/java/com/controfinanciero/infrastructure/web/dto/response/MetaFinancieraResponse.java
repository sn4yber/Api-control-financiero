package com.controfinanciero.infrastructure.web.dto.response;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response para meta financiera
 */
public record MetaFinancieraResponse(
        Long id,
        Long usuarioId,
        String nombre,
        String descripcion,
        BigDecimal montoObjetivo,
        BigDecimal montoActual,
        LocalDate fechaObjetivo,
        Prioridad prioridad,
        EstadoMeta estado,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime completedAt
) {
}
