package com.controfinanciero.domain.service;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.valueobject.Dinero;
import com.controfinanciero.domain.valueobject.Periodo;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * Domain Service: Calculador de Saldo.
 * Calcula el saldo actual a partir de los movimientos financieros.
 * El saldo NO se persiste, se calcula dinámicamente.
 */
public class CalculadorSaldo {

    /**
     * Calcula el saldo total del usuario basado en todos sus movimientos.
     * Fórmula: Ingresos - Gastos - Ahorros - Préstamos + Transferencias
     */
    public Dinero calcularSaldoTotal(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        BigDecimal saldo = movimientos.stream()
                .map(movimiento -> {
                    if (movimiento.incrementaSaldo()) {
                        return movimiento.getMonto();
                    } else {
                        return movimiento.getMonto().negate();
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Dinero(saldo, moneda);
    }

    /**
     * Calcula el saldo en un período específico.
     */
    public Dinero calcularSaldoEnPeriodo(List<MovimientoFinanciero> movimientos, Periodo periodo, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(periodo, "El periodo no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        List<MovimientoFinanciero> movimientosDelPeriodo = movimientos.stream()
                .filter(m -> periodo.contiene(m.getFechaMovimiento()))
                .toList();

        return calcularSaldoTotal(movimientosDelPeriodo, moneda);
    }

    /**
     * Calcula el total de ingresos.
     */
    public Dinero calcularTotalIngresos(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        BigDecimal total = movimientos.stream()
                .filter(MovimientoFinanciero::esIngreso)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Dinero(total, moneda);
    }

    /**
     * Calcula el total de gastos.
     */
    public Dinero calcularTotalGastos(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        BigDecimal total = movimientos.stream()
                .filter(MovimientoFinanciero::esGasto)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Dinero(total, moneda);
    }

    /**
     * Calcula el total de ahorros.
     */
    public Dinero calcularTotalAhorros(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        BigDecimal total = movimientos.stream()
                .filter(MovimientoFinanciero::esAhorro)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Dinero(total, moneda);
    }

    /**
     * Calcula el saldo disponible (sin contar ahorros).
     * Útil para saber cuánto dinero puede gastar el usuario.
     */
    public Dinero calcularSaldoDisponible(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Dinero ingresos = calcularTotalIngresos(movimientos, moneda);
        Dinero gastos = calcularTotalGastos(movimientos, moneda);
        Dinero ahorros = calcularTotalAhorros(movimientos, moneda);

        return ingresos.restar(gastos).restar(ahorros);
    }
}

