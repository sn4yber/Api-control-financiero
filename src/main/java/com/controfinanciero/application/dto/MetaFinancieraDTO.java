package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para representar una meta financiera
 */
public record MetaFinancieraDTO(
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
