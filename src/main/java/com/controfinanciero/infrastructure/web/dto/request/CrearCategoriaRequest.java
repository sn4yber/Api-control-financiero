package com.controfinanciero.infrastructure.web.dto.request;
import com.controfinanciero.domain.model.enums.TipoCategoria;
import jakarta.validation.constraints.*;

/**
 * ✅ Sin usuarioId (se obtiene del token JWT)
 */
public record CrearCategoriaRequest(

        @NotBlank(message = "Nombre es requerido")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @NotNull(message = "Tipo de categoría es requerido")
        TipoCategoria tipoCategoria,

        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Color debe ser formato hexadecimal (#RRGGBB)")
        String colorHex,

        @Size(max = 50, message = "El icono no puede exceder 50 caracteres")
        String icono
) {
}
