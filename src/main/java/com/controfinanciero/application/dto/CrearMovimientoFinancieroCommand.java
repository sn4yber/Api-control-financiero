package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Comando para crear un nuevo movimiento financiero
 */
public record CrearMovimientoFinancieroCommand(
        Long usuarioId,
        TipoMovimiento tipoMovimiento,
        BigDecimal monto,
        String descripcion,
        LocalDate fechaMovimiento,
        Long categoriaId,
        Long fuenteIngresoId,
        Long metaId,
        Boolean esRecurrente,
        String patronRecurrencia,
        String notas
) {
}

