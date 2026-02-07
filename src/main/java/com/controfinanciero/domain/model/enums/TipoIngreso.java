package com.controfinanciero.domain.model.enums;

/**
 * Tipo de ingreso principal del usuario.
 * Define la frecuencia con la que recibe dinero.
 */
public enum TipoIngreso {

    MONTHLY("Mensual", "Ingreso mensual fijo"),
    BIWEEKLY("Quincenal", "Ingreso cada dos semanas"),
    WEEKLY("Semanal", "Ingreso semanal"),
    PROJECT_BASED("Por proyecto", "Ingreso variable por proyecto completado"),
    VARIABLE("Variable", "Ingresos sin patrón fijo");

    private final String descripcion;
    private final String detalle;

    TipoIngreso(String descripcion, String detalle) {
        this.descripcion = descripcion;
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean esIngresoPredecible() {
        return this == MONTHLY || this == BIWEEKLY || this == WEEKLY;
    }

    public boolean esIngresoVariable() {
        return this == PROJECT_BASED || this == VARIABLE;
    }
}

