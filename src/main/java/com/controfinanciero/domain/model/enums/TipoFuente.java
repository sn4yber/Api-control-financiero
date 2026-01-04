package com.controfinanciero.domain.model.enums;

/**
 * Tipo de fuente de ingreso.
 * Clasifica el origen del dinero que recibe el usuario.
 */
public enum TipoFuente {

    SALARY("Salario", "Ingreso laboral fijo", true),
    FREELANCE("Freelance", "Trabajo independiente", true),
    LOAN("Préstamo", "Dinero prestado (no es ingreso real)", false),
    SCHOLARSHIP("Beca", "Apoyo educativo", true),
    SUBSIDY("Subsidio", "Ayuda del gobierno u organización", true),
    INVESTMENT("Inversión", "Retorno de inversiones", true),
    OTHER("Otro", "Otra fuente de ingreso", true);

    private final String descripcion;
    private final String detalle;
    private final boolean esIngresoReal;

    TipoFuente(String descripcion, String detalle, boolean esIngresoReal) {
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.esIngresoReal = esIngresoReal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean esIngresoReal() {
        return esIngresoReal;
    }

    public boolean esPrestamo() {
        return this == LOAN;
    }
}
