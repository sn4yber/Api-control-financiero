package com.controfinanciero.infrastructure.web.dto.request;

import com.controfinanciero.domain.model.enums.TipoFuente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request para crear una fuente de ingreso
 */
public record CrearFuenteIngresoRequest(
        @NotNull(message = "El ID de usuario es obligatorio")
        Long usuarioId,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El tipo de fuente es obligatorio")
        TipoFuente tipoFuente,

        Boolean esIngresoReal,
        Boolean activa
) {
}

