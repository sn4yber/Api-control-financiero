package com.controfinanciero.domain.model.enums;

/**
 * Prioridad de una meta financiera.
 * Ayuda al usuario a organizar sus objetivos por importancia.
 */
public enum Prioridad {

    LOW("Baja", "Puede esperar", 1),
    MEDIUM("Media", "Importante pero no urgente", 2),
    HIGH("Alta", "Importante y debe lograrse pronto", 3),
    CRITICAL("Crítica", "Urgente y prioritaria", 4);

    private final String descripcion;
    private final String detalle;
    private final int nivel;

    Prioridad(String descripcion, String detalle, int nivel) {
        this.descripcion = descripcion;
        this.detalle = detalle;
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDetalle() {
        return detalle;
    }

    public int getNivel() {
        return nivel;
    }

    public boolean esMasPrioritariaQue(Prioridad otra) {
        return this.nivel > otra.nivel;
    }

    public boolean esCritica() {
        return this == CRITICAL;
    }
}

