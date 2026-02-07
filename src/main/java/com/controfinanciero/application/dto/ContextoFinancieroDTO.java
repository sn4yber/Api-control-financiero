package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transferir datos de Contexto Financiero.
 */
public record ContextoFinancieroDTO(
        Long id,
        Long usuarioId,
        TipoIngreso tipoIngreso,
        boolean tieneIngresoVariable,
        BigDecimal porcentajeAhorroDeseado,
        PeriodoAnalisis periodoAnalisis,
        Integer diasPeriodoPersonalizado,
        String codigoMoneda,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

