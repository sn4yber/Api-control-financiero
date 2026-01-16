package com.controfinanciero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad de dominio: Movimiento Recurrente
 * Representa movimientos financieros programados automáticamente
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRecurrente {
    private Long id;
    private Long usuarioId;
    private String tipoMovimiento; // INCOME, EXPENSE, SAVINGS, LOAN, TRANSFER
    private BigDecimal monto;
    private String descripcion;
    private Long categoriaId;
    private Long fuenteIngresoId;
    private Long metaId;
    private String frecuencia; // DIARIA, SEMANAL, QUINCENAL, MENSUAL, ANUAL
    private Integer diaMes; // Para frecuencia MENSUAL: día del mes (1-31)
    private Integer diaSemana; // Para frecuencia SEMANAL: día de la semana (1=Lunes, 7=Domingo)
    private LocalDate fechaInicio;
    private LocalDate fechaFin; // Opcional: si es null, se ejecuta indefinidamente
    private LocalDate proximaEjecucion;
    private LocalDateTime ultimaEjecucion;
    private Boolean activo;
    private String notas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Calcula la próxima fecha de ejecución basada en la frecuencia
     */
    public void calcularProximaEjecucion() {
        if (ultimaEjecucion == null) {
            this.proximaEjecucion = fechaInicio;
            return;
        }

        LocalDate ultimaFecha = ultimaEjecucion.toLocalDate();

        switch (frecuencia) {
            case "DIARIA":
                this.proximaEjecucion = ultimaFecha.plusDays(1);
                break;
            case "SEMANAL":
                this.proximaEjecucion = ultimaFecha.plusWeeks(1);
                break;
            case "QUINCENAL":
                this.proximaEjecucion = ultimaFecha.plusDays(15);
                break;
            case "MENSUAL":
                this.proximaEjecucion = ultimaFecha.plusMonths(1);
                break;
            case "ANUAL":
                this.proximaEjecucion = ultimaFecha.plusYears(1);
                break;
            default:
                this.proximaEjecucion = ultimaFecha.plusMonths(1);
        }

        // Si hay fecha fin y la próxima ejecución la supera, desactivar
        if (fechaFin != null && this.proximaEjecucion.isAfter(fechaFin)) {
            this.activo = false;
        }
    }

    /**
     * Marca como ejecutado y calcula la próxima ejecución
     */
    public void marcarComoEjecutado() {
        this.ultimaEjecucion = LocalDateTime.now();
        calcularProximaEjecucion();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Verifica si debe ejecutarse hoy
     */
    public boolean debeEjecutarseHoy() {
        if (!activo) return false;
        if (proximaEjecucion == null) return false;

        LocalDate hoy = LocalDate.now();
        return !proximaEjecucion.isAfter(hoy);
    }
}

