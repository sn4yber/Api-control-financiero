package com.controfinanciero.application.dto;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;

import java.math.BigDecimal;

/**
 * Command para crear un contexto financiero.
 */
public record CrearContextoFinancieroCommand(
        Long usuarioId,
        TipoIngreso tipoIngreso,
        Boolean tieneIngresoVariable,
        BigDecimal porcentajeAhorroDeseado,
        PeriodoAnalisis periodoAnalisis,
        Integer diasPeriodoPersonalizado
) {
}
