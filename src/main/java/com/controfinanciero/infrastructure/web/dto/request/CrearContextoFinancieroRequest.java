package com.controfinanciero.infrastructure.web.dto.request;

import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * Request para crear un contexto financiero.
 */
public record CrearContextoFinancieroRequest(
        @NotNull(message = "Usuario ID es requerido")
        Long usuarioId,

        @NotNull(message = "Tipo de ingreso es requerido")
        TipoIngreso tipoIngreso,

        Boolean tieneIngresoVariable,

        @DecimalMin(value = "0.0", message = "El porcentaje debe ser mayor o igual a 0")
        @DecimalMax(value = "100.0", message = "El porcentaje debe ser menor o igual a 100")
        BigDecimal porcentajeAhorroDeseado,

        PeriodoAnalisis periodoAnalisis,

        @Min(value = 1, message = "Los días deben ser al menos 1")
        Integer diasPeriodoPersonalizado
) {
}


