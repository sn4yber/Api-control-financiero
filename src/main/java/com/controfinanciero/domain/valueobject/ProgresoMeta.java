package com.controfinanciero.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object que representa el progreso de una meta financiera.
 * Se calcula dinámicamente.
 */
public record ProgresoMeta(
        Dinero montoObjetivo,
        Dinero montoActual,
        Dinero montoRestante,
        BigDecimal porcentajeCompletado
) {

    /**
     * Constructor compacto con validaciones.
     */
    public ProgresoMeta {
        Objects.requireNonNull(montoObjetivo, "Monto objetivo no puede ser null");
        Objects.requireNonNull(montoActual, "Monto actual no puede ser null");
        Objects.requireNonNull(montoRestante, "Monto restante no puede ser null");
        Objects.requireNonNull(porcentajeCompletado, "Porcentaje no puede ser null");

        if (montoObjetivo.esNegativo()) {
            throw new IllegalArgumentException("El monto objetivo debe ser positivo");
        }
        if (montoActual.esNegativo()) {
            throw new IllegalArgumentException("El monto actual no puede ser negativo");
        }
    }

    /**
     * Crea un progreso de meta a partir de montos objetivo y actual.
     */
    public static ProgresoMeta desde(Dinero montoObjetivo, Dinero montoActual) {
        Dinero montoRestante = montoObjetivo.restar(montoActual);

        BigDecimal porcentaje = BigDecimal.ZERO;
        if (montoObjetivo.esPositivo()) {
            porcentaje = montoActual.cantidad()
                    .divide(montoObjetivo.cantidad(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new ProgresoMeta(montoObjetivo, montoActual, montoRestante, porcentaje);
    }

    /**
     * Verifica si la meta está completada (100% o más).
     */
    public boolean estaCompletada() {
        return porcentajeCompletado.compareTo(BigDecimal.valueOf(100)) >= 0;
    }

    /**
     * Verifica si la meta está en progreso (entre 1% y 99%).
     */
    public boolean estaEnProgreso() {
        return porcentajeCompletado.compareTo(BigDecimal.ZERO) > 0 &&
                porcentajeCompletado.compareTo(BigDecimal.valueOf(100)) < 0;
    }

    /**
     * Verifica si la meta no ha iniciado (0%).
     */
    public boolean noHaIniciado() {
        return porcentajeCompletado.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Verifica si ha superado el objetivo.
     */
    public boolean superoObjetivo() {
        return montoActual.esMayorQue(montoObjetivo);
    }
}
