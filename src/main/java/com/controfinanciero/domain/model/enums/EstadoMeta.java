package com.controfinanciero.domain.model.enums;

/**
 * Estado de una meta financiera.
 * Representa el ciclo de vida de una meta.
 */
public enum EstadoMeta {
    ACTIVE("Activa", "Meta en progreso"),
    COMPLETED("Completada", "Meta alcanzada exitosamente"),
    CANCELLED("Cancelada", "Meta abandonada"),
    PAUSED("Pausada", "Meta temporalmente suspendida");

    private final String descripcion;
    private final String detalle;

    EstadoMeta(String descripcion, String detalle) {
        this.descripcion = descripcion;
        this.detalle = detalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public boolean estaActiva() {
        return this == ACTIVE;
    }
    public boolean estaCompletada() {
        return this == COMPLETED;
    }

    public boolean puedeCambiarA(EstadoMeta nuevoEstado) {
        return switch (this) {
            case ACTIVE -> nuevoEstado == COMPLETED || nuevoEstado == CANCELLED || nuevoEstado == PAUSED;
            case PAUSED -> nuevoEstado == ACTIVE || nuevoEstado == CANCELLED;
            case COMPLETED, CANCELLED -> false; // Estados finales
        };
    }

}
