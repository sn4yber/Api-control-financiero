package com.controfinanciero.domain.service;

import com.controfinanciero.domain.model.MetaFinanciera;
import com.controfinanciero.domain.valueobject.Dinero;
import com.controfinanciero.domain.valueobject.ProgresoMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.Objects;

/**
 * Domain Service: Calculador de Progreso de Meta.
 * Calcula el progreso, tiempo restante y proyecciones de metas financieras.
 */
public class CalculadorProgresoMeta {

    /**
     * Calcula el progreso completo de una meta financiera.
     */
    public ProgresoMeta calcularProgreso(MetaFinanciera meta, Currency moneda) {
        Objects.requireNonNull(meta, "La meta no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        Dinero montoObjetivo = new Dinero(meta.getMontoObjetivo(), moneda);
        Dinero montoActual = new Dinero(meta.getMontoActual(), moneda);

        return ProgresoMeta.desde(montoObjetivo, montoActual);
    }

    /**
     * Calcula cuántos días faltan para la fecha objetivo.
     * Retorna null si la meta no tiene fecha objetivo.
     */
    public Long calcularDiasRestantes(MetaFinanciera meta) {
        Objects.requireNonNull(meta, "La meta no puede ser null");

        if (meta.getFechaObjetivo() == null) {
            return null;
        }

        LocalDate hoy = LocalDate.now();
        LocalDate fechaObjetivo = meta.getFechaObjetivo();

        if (fechaObjetivo.isBefore(hoy)) {
            return 0L; // Meta vencida
        }

        return ChronoUnit.DAYS.between(hoy, fechaObjetivo);
    }

    /**
     * Calcula cuánto dinero se debe ahorrar diariamente para cumplir la meta.
     * Retorna null si la meta no tiene fecha objetivo.
     */
    public Dinero calcularAhorroDiarioNecesario(MetaFinanciera meta, Currency moneda) {
        Objects.requireNonNull(meta, "La meta no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        Long diasRestantes = calcularDiasRestantes(meta);

        if (diasRestantes == null || diasRestantes == 0) {
            return null;
        }

        BigDecimal montoRestante = meta.getMontoObjetivo().subtract(meta.getMontoActual());
        BigDecimal ahorroDiario = montoRestante.divide(
                BigDecimal.valueOf(diasRestantes),
                2,
                RoundingMode.HALF_UP
        );

        return new Dinero(ahorroDiario, moneda);
    }

    /**
     * Calcula cuánto dinero se debe ahorrar mensualmente para cumplir la meta.
     */
    public Dinero calcularAhorroMensualNecesario(MetaFinanciera meta, Currency moneda) {
        Objects.requireNonNull(meta, "La meta no puede ser null");
        Objects.requireNonNull(moneda, "La moneda no puede ser null");

        Dinero ahorroDiario = calcularAhorroDiarioNecesario(meta, moneda);

        if (ahorroDiario == null) {
            return null;
        }

        return ahorroDiario.multiplicar(BigDecimal.valueOf(30));
    }

    /**
     * Estima en cuántos días se completará la meta según el ritmo actual.
     * Requiere al menos 2 datos: monto actual > 0 y días transcurridos.
     * Retorna null si no hay suficiente información.
     */
    public Long estimarDiasParaCompletar(MetaFinanciera meta) {
        Objects.requireNonNull(meta, "La meta no puede ser null");

        if (meta.getMontoActual().compareTo(BigDecimal.ZERO) == 0) {
            return null; // No hay progreso aún
        }

        if (meta.getCreatedAt() == null) {
            return null; // No se puede calcular sin fecha de creación
        }

        LocalDate fechaCreacion = meta.getCreatedAt().toLocalDate();
        LocalDate hoy = LocalDate.now();
        long diasTranscurridos = ChronoUnit.DAYS.between(fechaCreacion, hoy);

        if (diasTranscurridos == 0) {
            return null; // Muy reciente para estimar
        }

        // Calcular ahorro diario promedio
        BigDecimal ahorroDiarioPromedio = meta.getMontoActual()
                .divide(BigDecimal.valueOf(diasTranscurridos), 4, RoundingMode.HALF_UP);

        // Calcular cuánto falta
        BigDecimal montoRestante = meta.getMontoObjetivo().subtract(meta.getMontoActual());

        // Estimar días necesarios
        BigDecimal diasEstimados = montoRestante.divide(ahorroDiarioPromedio, 0, RoundingMode.CEILING);

        return diasEstimados.longValue();
    }

    /**
     * Verifica si la meta está en riesgo de no cumplirse a tiempo.
     * Una meta está en riesgo si el ritmo actual no es suficiente para llegar a tiempo.
     */
    public boolean estaEnRiesgo(MetaFinanciera meta) {
        Objects.requireNonNull(meta, "La meta no puede ser null");

        Long diasRestantes = calcularDiasRestantes(meta);
        Long diasEstimados = estimarDiasParaCompletar(meta);

        if (diasRestantes == null || diasEstimados == null) {
            return false; // No hay suficiente información
        }

        return diasEstimados > diasRestantes;
    }
}

