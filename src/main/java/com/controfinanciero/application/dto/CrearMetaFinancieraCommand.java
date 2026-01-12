package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Comando para crear una nueva meta financiera
 */
public record CrearMetaFinancieraCommand(
        Long usuarioId,
        String nombre,
        String descripcion,
        BigDecimal montoObjetivo,
        LocalDate fechaObjetivo,
        Prioridad prioridad,
        EstadoMeta estado
) {
}
