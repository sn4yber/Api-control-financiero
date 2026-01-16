package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Movimiento Recurrente
 */
@Entity
@Table(name = "movimientos_recurrentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoRecurrenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo_movimiento", nullable = false, length = 20)
    private String tipoMovimiento;

    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "descripcion", nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria_id")
    private Long categoriaId;

    @Column(name = "fuente_ingreso_id")
    private Long fuenteIngresoId;

    @Column(name = "meta_id")
    private Long metaId;

    @Column(name = "frecuencia", nullable = false, length = 20)
    private String frecuencia;

    @Column(name = "dia_mes")
    private Integer diaMes;

    @Column(name = "dia_semana")
    private Integer diaSemana;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "proxima_ejecucion", nullable = false)
    private LocalDate proximaEjecucion;

    @Column(name = "ultima_ejecucion")
    private LocalDateTime ultimaEjecucion;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activo == null) activo = true;
        if (proximaEjecucion == null) proximaEjecucion = fechaInicio;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

