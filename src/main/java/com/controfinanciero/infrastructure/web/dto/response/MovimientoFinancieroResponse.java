package com.controfinanciero.infrastructure.web.dto.response;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response para movimiento financiero
 */
public record MovimientoFinancieroResponse(
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

