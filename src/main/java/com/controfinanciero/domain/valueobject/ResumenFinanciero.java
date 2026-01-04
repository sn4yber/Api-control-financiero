package com.controfinanciero.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object que representa un resumen financiero calculado.
 * No se persiste, se genera bajo demanda.
 */
public record ResumenFinanciero(
    Dinero totalIngresos,
    Dinero totalGastos,
    Dinero totalAhorros,
    Dinero saldoDisponible,
    Periodo periodo
) {

    /**
     * Constructor compacto con validaciones.
     */
    public ResumenFinanciero {
        Objects.requireNonNull(totalIngresos, "Total de ingresos no puede ser null");
        Objects.requireNonNull(totalGastos, "Total de gastos no puede ser null");
        Objects.requireNonNull(totalAhorros, "Total de ahorros no puede ser null");
        Objects.requireNonNull(saldoDisponible, "Saldo disponible no puede ser null");
        Objects.requireNonNull(periodo, "Periodo no puede ser null");
    }

    /**
     * Calcula el porcentaje de ahorro sobre los ingresos.
     */
    public BigDecimal porcentajeAhorro() {
        if (totalIngresos.esCero()) {
            return BigDecimal.ZERO;
        }
        return totalAhorros.cantidad()
            .divide(totalIngresos.cantidad(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Calcula el porcentaje gastado sobre los ingresos.
     */
    public BigDecimal porcentajeGasto() {
        if (totalIngresos.esCero()) {
            return BigDecimal.ZERO;
        }
        return totalGastos.cantidad()
            .divide(totalIngresos.cantidad(), 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Verifica si hay déficit (gastos mayores que ingresos).
     */
    public boolean tieneDeficit() {
        return totalGastos.esMayorQue(totalIngresos);
    }

    /**
     * Verifica si hay superávit (ingresos mayores que gastos).
     */
    public boolean tieneSuperavit() {
        return totalIngresos.esMayorQue(totalGastos);
    }

    /**
     * Calcula el balance (ingresos - gastos - ahorros).
     */
    public Dinero balance() {
        return totalIngresos.restar(totalGastos).restar(totalAhorros);
    }
}
