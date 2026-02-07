package com.controfinanciero.infrastructure.web.dto.response;


import com.controfinanciero.domain.model.enums.PeriodoAnalisis;
import com.controfinanciero.domain.model.enums.TipoIngreso;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public record ContextoFinancieroResponse(
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
