package com.controfinanciero.domain.model.enums;

/**
 * Periodo de análisis financiero del usuario.
 * Define el marco temporal para generar resúmenes.
 */
public enum PeriodoAnalisis {

    MONTHLY("Mensual", "Análisis mes a mes"),
    BIWEEKLY("Quincenal", "Análisis cada dos semanas"),
    CUSTOM("Personalizado", "Periodo definido por el usuario");

    private final String descripcion;
    private final String detalle;

    PeriodoAnalisis(String descripcion, String detalle) {
        this.descripcion = descripcion;
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean requiereDiasPersonalizados() {
        return this == CUSTOM;
    }
}

