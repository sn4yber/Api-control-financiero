package com.controfinanciero.domain.service;

import com.controfinanciero.domain.model.MovimientoFinanciero;
import com.controfinanciero.domain.valueobject.Dinero;
import com.controfinanciero.domain.valueobject.Periodo;
import com.controfinanciero.domain.valueobject.ResumenFinanciero;

import java.util.Currency;
import java.util.List;
import java.util.Objects;

/**
 * Domain Service: Generador de Resumen Financiero.
 * Genera resúmenes consolidados de la situación financiera del usuario.
 * Los resúmenes no se persisten, se calculan dinámicamente.
 */
public class GeneradorResumen {

    private final CalculadorSaldo calculadorSaldo;

    public GeneradorResumen(CalculadorSaldo calculadorSaldo) {
        this.calculadorSaldo = Objects.requireNonNull(calculadorSaldo, "CalculadorSaldo no puede ser null");
    }

    /**
     * Genera un resumen financiero para un período específico.
     */
    public ResumenFinanciero generar(List<MovimientoFinanciero> movimientos, Periodo periodo, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(periodo, "El periodo no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        // Filtrar movimientos del período
        List<MovimientoFinanciero> movimientosDelPeriodo = movimientos.stream()
                .filter(m -> periodo.contiene(m.getFechaMovimiento()))
                .toList();

        // Calcular totales
        Dinero totalIngresos = calculadorSaldo.calcularTotalIngresos(movimientosDelPeriodo, moneda);
        Dinero totalGastos = calculadorSaldo.calcularTotalGastos(movimientosDelPeriodo, moneda);
        Dinero totalAhorros = calculadorSaldo.calcularTotalAhorros(movimientosDelPeriodo, moneda);
        Dinero saldoDisponible = calculadorSaldo.calcularSaldoDisponible(movimientosDelPeriodo, moneda);

        return new ResumenFinanciero(
                totalIngresos,
                totalGastos,
                totalAhorros,
                saldoDisponible,
                periodo
        );
    }

    /**
     * Genera un resumen financiero general (todos los movimientos).
     */
    public ResumenFinanciero generarGeneral(List<MovimientoFinanciero> movimientos, Currency moneda) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        if (movimientos.isEmpty()) {
            Dinero cero = new Dinero(java.math.BigDecimal.ZERO, moneda);
            java.time.LocalDate hoy = java.time.LocalDate.now();
            Periodo periodoVacio = new Periodo(hoy, hoy);
            return new ResumenFinanciero(cero, cero, cero, cero, periodoVacio);
        }

        // Determinar período automáticamente
        java.time.LocalDate fechaInicio = movimientos.stream()
                .map(MovimientoFinanciero::getFechaMovimiento)
                .min(java.time.LocalDate::compareTo)
                .orElse(java.time.LocalDate.now());

        java.time.LocalDate fechaFin = movimientos.stream()
                .map(MovimientoFinanciero::getFechaMovimiento)
                .max(java.time.LocalDate::compareTo)
                .orElse(java.time.LocalDate.now());

        Periodo periodo = new Periodo(fechaInicio, fechaFin);

        return generar(movimientos, periodo, moneda);
    }

    /**
     * Genera un resumen comparativo entre dos períodos.
     * Útil para ver evolución mensual, quincenal, etc.
     */
    public ResumenComparativo generarComparativo(
            List<MovimientoFinanciero> movimientos,
            Periodo periodoActual,
            Periodo periodoAnterior,
            Currency moneda
    ) {
        Objects.requireNonNull(movimientos, "La lista de movimientos no puede ser null");
        Objects.requireNonNull(periodoActual, "El periodo actual no puede ser null");
        Objects.requireNonNull(periodoAnterior, "El periodo anterior no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        ResumenFinanciero resumenActual = generar(movimientos, periodoActual, moneda);
        ResumenFinanciero resumenAnterior = generar(movimientos, periodoAnterior, moneda);

        return new ResumenComparativo(resumenActual, resumenAnterior);
    }

    /**
     * Record que representa una comparación entre dos resúmenes.
     */
    public record ResumenComparativo(
            ResumenFinanciero periodoActual,
            ResumenFinanciero periodoAnterior
    ) {
        public Dinero diferenciaIngresos() {
            return periodoActual.totalIngresos().restar(periodoAnterior.totalIngresos());
        }

        public Dinero diferenciaGastos() {
            return periodoActual.totalGastos().restar(periodoAnterior.totalGastos());
        }

        public Dinero diferenciaAhorros() {
            return periodoActual.totalAhorros().restar(periodoAnterior.totalAhorros());
        }

        public boolean mejoroSituacion() {
            return periodoActual.saldoDisponible().esMayorQue(periodoAnterior.saldoDisponible());
        }
    }
}

