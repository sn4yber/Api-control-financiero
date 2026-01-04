package com.controfinanciero.domain.valueobject;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Value Object que representa un periodo de tiempo (rango de fechas).
 * Inmutable y con validaciones de consistencia.
 */
public record Periodo(LocalDate fechaInicio, LocalDate fechaFin) {

    /**
     * Constructor compacto con validaciones.
     */
    public Periodo {
        Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser null");
        Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser null");

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                "La fecha de inicio no puede ser posterior a la fecha de fin"
            );
        }
    }

    /**
     * Crea un periodo del mes actual.
     */
    public static Periodo mesActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate primerDia = hoy.withDayOfMonth(1);
        LocalDate ultimoDia = hoy.withDayOfMonth(hoy.lengthOfMonth());
        return new Periodo(primerDia, ultimoDia);
    }

    /**
     * Crea un periodo de un mes específico.
     */
    public static Periodo mes(int year, int month) {
        LocalDate primerDia = LocalDate.of(year, month, 1);
        LocalDate ultimoDia = primerDia.withDayOfMonth(primerDia.lengthOfMonth());
        return new Periodo(primerDia, ultimoDia);
    }

    /**
     * Crea un periodo quincenal (primera quincena del mes actual).
     */
    public static Periodo primeraQuincenaActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate primerDia = hoy.withDayOfMonth(1);
        LocalDate dia15 = hoy.withDayOfMonth(15);
        return new Periodo(primerDia, dia15);
    }

    /**
     * Crea un periodo quincenal (segunda quincena del mes actual).
     */
    public static Periodo segundaQuincenaActual() {
        LocalDate hoy = LocalDate.now();
        LocalDate dia16 = hoy.withDayOfMonth(16);
        LocalDate ultimoDia = hoy.withDayOfMonth(hoy.lengthOfMonth());
        return new Periodo(dia16, ultimoDia);
    }

    /**
     * Crea un periodo desde hace N días hasta hoy.
     */
    public static Periodo ultimosDias(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate inicio = hoy.minusDays(dias);
        return new Periodo(inicio, hoy);
    }

    /**
     * Crea un periodo personalizado con N días de duración desde hoy.
     */
    public static Periodo personalizado(int dias) {
        LocalDate hoy = LocalDate.now();
        LocalDate fin = hoy.plusDays(dias);
        return new Periodo(hoy, fin);
    }

    /**
     * Calcula la cantidad de días en este periodo (inclusivo).
     */
    public long cantidadDias() {
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    /**
     * Verifica si una fecha está dentro de este periodo.
     */
    public boolean contiene(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    /**
     * Verifica si este periodo se superpone con otro.
     */
    public boolean seSuperponeCon(Periodo otro) {
        return !this.fechaFin.isBefore(otro.fechaInicio) &&
               !otro.fechaFin.isBefore(this.fechaInicio);
    }

    /**
     * Verifica si este periodo está en el pasado.
     */
    public boolean esPasado() {
        return fechaFin.isBefore(LocalDate.now());
    }

    /**
     * Verifica si este periodo incluye el día de hoy.
     */
    public boolean incluyeHoy() {
        return contiene(LocalDate.now());
    }

    @Override
    public String toString() {
        return String.format("%s a %s (%d días)", fechaInicio, fechaFin, cantidadDias());
    }
}

