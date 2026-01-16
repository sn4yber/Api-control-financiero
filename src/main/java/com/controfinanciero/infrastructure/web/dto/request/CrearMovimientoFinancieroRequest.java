package com.controfinanciero.infrastructure.web.dto.request;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request para crear un movimiento financiero
 * ✅ Sin usuarioId (se obtiene del token JWT)
 */
public record CrearMovimientoFinancieroRequest(

        @NotNull(message = "El tipo de movimiento es obligatorio")
        TipoMovimiento tipoMovimiento,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto,

        String descripcion,

        @NotNull(message = "La fecha del movimiento es obligatoria")
        LocalDate fechaMovimiento,

        Long categoriaId,
        Long fuenteIngresoId,
        Long metaId,
        Boolean esRecurrente,
        String patronRecurrencia,
        String notas
) {
}

