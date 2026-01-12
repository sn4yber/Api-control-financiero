package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO para representar un movimiento financiero
 */
public record MovimientoFinancieroDTO(
        Long id,
        Long usuarioId,
        TipoMovimiento tipoMovimiento,
        BigDecimal monto,
        String descripcion,
        LocalDate fechaMovimiento,
        Long categoriaId,
        String categoriaNombre,
        Long fuenteIngresoId,
        String fuenteIngresoNombre,
        Long metaId,
        String metaNombre,
        Boolean esRecurrente,
        String patronRecurrencia,
        String notas,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

