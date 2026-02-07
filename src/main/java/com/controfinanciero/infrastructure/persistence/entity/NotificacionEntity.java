package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA: Notificación
 */
@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "mensaje", nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(name = "leida", nullable = false)
    private Boolean leida = false;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Metadata adicional para notificaciones de metas compartidas
    @Column(name = "meta_id")
    private Long metaId;

    @Column(name = "usuario_invitador", length = 100)
    private String usuarioInvitador;

    @Column(name = "meta_nombre", length = 200)
    private String metaNombre;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (leida == null) leida = false;
        if (fechaEnvio == null) fechaEnvio = LocalDateTime.now();
    }
}

