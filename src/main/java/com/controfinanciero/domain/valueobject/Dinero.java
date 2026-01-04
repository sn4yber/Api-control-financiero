package com.controfinanciero.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

/**
 * Value Object que representa una cantidad de dinero con su moneda.
 * Inmutable y con operaciones aritméticas seguras.
 */
public record Dinero(BigDecimal cantidad, Currency moneda) {

    /**
     * Constructor compacto con validaciones.
     */
    public Dinero {
        Objects.requireNonNull(cantidad, "La cantidad no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        // Redondear a 2 decimales
        cantidad = cantidad.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Constructor conveniente para moneda COP (pesos colombianos).
     */
    public static Dinero cop(BigDecimal cantidad) {
        return new Dinero(cantidad, Currency.getInstance("COP"));
    }

    /**
     * Constructor conveniente para moneda COP desde double.
     */
    public static Dinero cop(double cantidad) {
        return cop(BigDecimal.valueOf(cantidad));
    }

    /**
     * Constructor conveniente para cero en COP.
     */
    public static Dinero ceroCop() {
        return cop(BigDecimal.ZERO);
    }

    /**
     * Suma este dinero con otro (deben ser de la misma moneda).
     */
    public Dinero sumar(Dinero otro) {
        validarMismaMoneda(otro);
        return new Dinero(this.cantidad.add(otro.cantidad), this.moneda);
    }

    /**
     * Resta otro dinero de este (deben ser de la misma moneda).
     */
    public Dinero restar(Dinero otro) {
        validarMismaMoneda(otro);
        return new Dinero(this.cantidad.subtract(otro.cantidad), this.moneda);
    }

    /**
     * Multiplica este dinero por un factor.
     */
    public Dinero multiplicar(BigDecimal factor) {
        return new Dinero(this.cantidad.multiply(factor), this.moneda);
    }

    /**
     * Calcula el porcentaje de este dinero.
     */
    public Dinero porcentaje(BigDecimal porcentaje) {
        BigDecimal factor = porcentaje.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return multiplicar(factor);
    }

    /**
     * Verifica si este dinero es mayor que otro.
     */
    public boolean esMayorQue(Dinero otro) {
        validarMismaMoneda(otro);
        return this.cantidad.compareTo(otro.cantidad) > 0;
    }

    /**
     * Verifica si este dinero es menor que otro.
     */
    public boolean esMenorQue(Dinero otro) {
        validarMismaMoneda(otro);
        return this.cantidad.compareTo(otro.cantidad) < 0;
    }

    /**
     * Verifica si este dinero es cero.
     */
    public boolean esCero() {
        return this.cantidad.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Verifica si este dinero es positivo.
     */
    public boolean esPositivo() {
        return this.cantidad.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verifica si este dinero es negativo.
     */
    public boolean esNegativo() {
        return this.cantidad.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Valida que otro dinero tenga la misma moneda.
     */
    private void validarMismaMoneda(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException(
                String.format("No se pueden operar monedas diferentes: %s y %s",
                    this.moneda.getCurrencyCode(), otro.moneda.getCurrencyCode())
            );
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s", moneda.getSymbol(), cantidad);
    }
}

