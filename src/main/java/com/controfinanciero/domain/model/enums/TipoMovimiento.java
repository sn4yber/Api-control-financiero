package com.controfinanciero.domain.model.enums;

/**
 * Tipo de movimiento financiero.
 * Representa todas las posibles operaciones con dinero.
 */
public enum TipoMovimiento {

    INCOME("Ingreso", "Entrada de dinero", true),
    EXPENSE("Gasto", "Salida de dinero", false),
    SAVINGS("Ahorro", "Dinero apartado", false),
    LOAN("Préstamo", "Dinero prestado (entrada temporal)", true),
    TRANSFER("Transferencia", "Movimiento entre cuentas", false);

    private final String descripcion;
    private final String detalle;
    private final boolean incrementaSaldo;

    TipoMovimiento(String descripcion, String detalle, boolean incrementaSaldo) {
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.incrementaSaldo = incrementaSaldo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean incrementaSaldo() {
        return incrementaSaldo;
    }

    public boolean esIngreso() {
        return this == INCOME;
    }

    public boolean esGasto() {
        return this == EXPENSE;
    }

    public boolean esAhorro() {
        return this == SAVINGS;
    }

    public boolean esPrestamo() {
        return this == LOAN;
    }
}

