package com.controfinanciero.domain.model.enums;

/**
 * Tipo de categoría para clasificar movimientos.
 * Define el propósito general del movimiento financiero.
 */
public enum TipoCategoria {

    EXPENSE("Gasto", "Dinero gastado en consumo o servicios"),
    SAVINGS("Ahorro", "Dinero apartado para el futuro"),
    INVESTMENT("Inversión", "Dinero destinado a generar retorno"),
    DEBT("Deuda", "Dinero destinado a pagar deudas");

    private final String descripcion;
    private final String detalle;

    TipoCategoria(String descripcion, String detalle) {
        this.descripcion = descripcion;
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean esGasto() {
        return this == EXPENSE;
    }

    public boolean esAhorro() {
        return this == SAVINGS;
    }

    public boolean esInversion() {
        return this == INVESTMENT;
    }
}

