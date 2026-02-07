package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 📜 Auditoría - Historial de cambios
 * Registra QUIÉN modificó QUÉ y CUÁNDO
 */
@Entity
@Table(name = "auditoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroAuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    /**
     * Tipo de entidad: MOVIMIENTO, META, CATEGORIA, etc.
     */
    @Column(nullable = false)
    private String tipoEntidad;

    /**
     * ID de la entidad modificada
     */
    @Column(nullable = false)
    private Long entidadId;

    /**
     * Acción: CREATE, UPDATE, DELETE
     */
    @Column(nullable = false)
    private String accion;

    /**
     * Estado anterior (JSON)
     */
    @Column(columnDefinition = "TEXT")
    private String valorAnterior;

    /**
     * Estado nuevo (JSON)
     */
    @Column(columnDefinition = "TEXT")
    private String valorNuevo;

    /**
     * IP del usuario
     */
    private String ipAddress;

    /**
     * User Agent
     */
    private String userAgent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

