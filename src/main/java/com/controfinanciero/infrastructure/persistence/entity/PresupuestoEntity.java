package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Presupuesto
 */
@Entity
@Table(name = "presupuestos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresupuestoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "categoria_id", nullable = false)
    private Long categoriaId;

    @Column(name = "categoria_nombre", nullable = false, length = 100)
    private String categoriaNombre;

    @Column(name = "limite_mensual", nullable = false, precision = 15, scale = 2)
    private BigDecimal limiteMensual;

    @Column(name = "gasto_actual", nullable = false, precision = 15, scale = 2)
    private BigDecimal gastoActual = BigDecimal.ZERO;

    @Column(name = "periodo", nullable = false, length = 7)
    private String periodo; // "2026-01", "2026-02"

    @Column(name = "alerta_enviada", nullable = false)
    private Boolean alertaEnviada = false;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (gastoActual == null) gastoActual = BigDecimal.ZERO;
        if (alertaEnviada == null) alertaEnviada = false;
        if (activo == null) activo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

