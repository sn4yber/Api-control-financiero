package com.controfinanciero.infrastructure.web.dto.request;

import com.controfinanciero.domain.model.enums.Prioridad;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request para crear una meta financiera
 * ✅ Sin usuarioId (se obtiene del token JWT)
 */
public record CrearMetaFinancieraRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String descripcion,

        @NotNull(message = "El monto objetivo es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal montoObjetivo,

        LocalDate fechaObjetivo,

        @NotNull(message = "La prioridad es obligatoria")
        Prioridad prioridad
) {
}
