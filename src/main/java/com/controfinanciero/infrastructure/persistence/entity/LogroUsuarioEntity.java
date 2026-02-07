package com.controfinanciero.infrastructure.persistence.entity;

import com.controfinanciero.domain.model.enums.TipoLogro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA: Logro desbloqueado por el usuario
 */
@Entity
@Table(name = "logros_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogroUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_logro", nullable = false)
    private TipoLogro tipoLogro;

    @Column(name = "desbloqueado_at", nullable = false)
    private LocalDateTime desbloqueadoAt;

    @Column(name = "reclamado", nullable = false)
    private Boolean reclamado = false;

    @Column(name = "progreso_actual")
    private Integer progresoActual = 0;

    @Column(name = "progreso_requerido")
    private Integer progresoRequerido = 1;

    @PrePersist
    protected void onCreate() {
        if (desbloqueadoAt == null) {
            desbloqueadoAt = LocalDateTime.now();
        }
        if (reclamado == null) {
            reclamado = false;
        }
    }

    public boolean estaCompleto() {
        return progresoActual >= progresoRequerido;
    }
}

