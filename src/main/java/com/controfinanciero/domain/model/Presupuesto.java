package com.controfinanciero.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * Entidad de dominio: Presupuesto
 * Permite al usuario definir límites de gasto por categoría
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presupuesto {
    private Long id;
    private Long usuarioId;
    private Long categoriaId;
    private String categoriaNombre;
    private BigDecimal limiteMensual;
    private BigDecimal gastoActual;
    private String periodo; // "2026-01", "2026-02", etc.
    private Boolean alertaEnviada;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Calcula el porcentaje de presupuesto usado
     */
    public BigDecimal calcularPorcentajeUsado() {
        if (limiteMensual == null || limiteMensual.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return gastoActual.divide(limiteMensual, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * Verifica si el presupuesto está excedido
     */
    public boolean estaExcedido() {
        return gastoActual.compareTo(limiteMensual) >= 0;
    }

    /**
     * Verifica si el presupuesto está cerca del límite (90%)
     */
    public boolean estaCercaDelLimite() {
        BigDecimal limite90 = limiteMensual.multiply(BigDecimal.valueOf(0.9));
        return gastoActual.compareTo(limite90) >= 0;
    }

    /**
     * Actualiza el gasto actual
     */
    public void actualizarGasto(BigDecimal nuevoGasto) {
        this.gastoActual = this.gastoActual.add(nuevoGasto);
        this.updatedAt = LocalDateTime.now();
    }
}

