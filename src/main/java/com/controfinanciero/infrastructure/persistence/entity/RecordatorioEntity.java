package com.controfinanciero.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad JPA: Recordatorio de Pago
 */
@Entity
@Table(name = "recordatorios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordatorioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long usuarioId;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "monto", precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "recurrente", nullable = false)
    private Boolean recurrente = false;

    @Column(name = "frecuencia")
    private String frecuencia; // MENSUAL, SEMANAL, ANUAL

    @Column(name = "dias_anticipacion")
    private Integer diasAnticipacion = 3;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "pagado", nullable = false)
    private Boolean pagado = false;

    @Column(name = "movimiento_id")
    private Long movimientoId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (activo == null) activo = true;
        if (pagado == null) pagado = false;
        if (recurrente == null) recurrente = false;
        if (diasAnticipacion == null) diasAnticipacion = 3;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean debeNotificar() {
        if (pagado || !activo) return false;
        LocalDate fechaNotificacion = fechaPago.minusDays(diasAnticipacion);
        return LocalDate.now().isAfter(fechaNotificacion) || LocalDate.now().isEqual(fechaNotificacion);
    }
}

