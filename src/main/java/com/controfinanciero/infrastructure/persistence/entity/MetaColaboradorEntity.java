package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Colaborador en Meta Compartida
 */
@Entity
@Table(name = "meta_colaboradores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaColaboradorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meta_id", nullable = false)
    private Long metaId;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "es_creador", nullable = false)
    private Boolean esCreador = false;

    @Column(name = "aporte_total", precision = 15, scale = 2)
    private BigDecimal aporteTotal = BigDecimal.ZERO;

    @Column(name = "porcentaje_aporte")
    private Double porcentajeAporte = 0.0;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "invitado_at")
    private LocalDateTime invitadoAt;

    @Column(name = "aceptado_at")
    private LocalDateTime aceptadoAt;

    @PrePersist
    protected void onCreate() {
        if (esCreador == null) esCreador = false;
        if (aporteTotal == null) aporteTotal = BigDecimal.ZERO;
        if (porcentajeAporte == null) porcentajeAporte = 0.0;
        if (activo == null) activo = true;
        if (invitadoAt == null) invitadoAt = LocalDateTime.now();
    }
}

