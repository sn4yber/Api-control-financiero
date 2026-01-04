package com.controfinanciero.domain.model;

import com.controfinanciero.domain.model.enums.EstadoMeta;
import com.controfinanciero.domain.model.enums.Prioridad;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio: Meta Financiera.
 * Representa un objetivo económico del usuario con seguimiento de progreso.
 * Una meta motiva, no solo calcula.
 */
public class MetaFinanciera {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private BigDecimal montoObjetivo;
    private BigDecimal montoActual;
    private LocalDate fechaObjetivo;
    private Prioridad prioridad;
    private EstadoMeta estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    // Constructor vacío requerido
    protected MetaFinanciera() {
    }

    // Constructor para creación
    public MetaFinanciera(
            Long usuarioId,
            String nombre,
            BigDecimal montoObjetivo,
            LocalDate fechaObjetivo,
            Prioridad prioridad
    ) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "UsuarioId no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.montoObjetivo = Objects.requireNonNull(montoObjetivo, "Monto objetivo no puede ser null");
        this.fechaObjetivo = fechaObjetivo;
        this.prioridad = Objects.requireNonNull(prioridad, "Prioridad no puede ser null");
        this.montoActual = BigDecimal.ZERO;
        this.estado = EstadoMeta.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validarMontoObjetivo();
        validarNombre();
    }

    // Métodos de negocio
    public void agregarMonto(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }
        if (!estado.estaActiva()) {
            throw new IllegalStateException("No se puede agregar dinero a una meta no activa");
        }

        this.montoActual = this.montoActual.add(monto);
        this.updatedAt = LocalDateTime.now();

        if (montoActual.compareTo(montoObjetivo) >= 0) {
            completar();
        }
    }

    public void completar() {
        if (!estado.puedeCambiarA(EstadoMeta.COMPLETED)) {
            throw new IllegalStateException("No se puede completar esta meta desde su estado actual");
        }
        this.estado = EstadoMeta.COMPLETED;
        this.completedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void cancelar() {
        if (!estado.puedeCambiarA(EstadoMeta.CANCELLED)) {
            throw new IllegalStateException("No se puede cancelar esta meta desde su estado actual");
        }
        this.estado = EstadoMeta.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void pausar() {
        if (!estado.puedeCambiarA(EstadoMeta.PAUSED)) {
            throw new IllegalStateException("No se puede pausar esta meta desde su estado actual");
        }
        this.estado = EstadoMeta.PAUSED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reactivar() {
        if (!estado.puedeCambiarA(EstadoMeta.ACTIVE)) {
            throw new IllegalStateException("No se puede reactivar esta meta desde su estado actual");
        }
        this.estado = EstadoMeta.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void actualizarInformacion(String nombre, String descripcion) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.descripcion = descripcion;
        this.updatedAt = LocalDateTime.now();
        validarNombre();
    }

    public void cambiarPrioridad(Prioridad nuevaPrioridad) {
        this.prioridad = Objects.requireNonNull(nuevaPrioridad, "Prioridad no puede ser null");
        this.updatedAt = LocalDateTime.now();
    }

    public void extenderFecha(LocalDate nuevaFecha) {
        if (nuevaFecha != null && nuevaFecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha objetivo no puede estar en el pasado");
        }
        this.fechaObjetivo = nuevaFecha;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal calcularMontoRestante() {
        BigDecimal restante = montoObjetivo.subtract(montoActual);
        return restante.compareTo(BigDecimal.ZERO) > 0 ? restante : BigDecimal.ZERO;
    }

    public BigDecimal calcularPorcentajeProgreso() {
        if (montoObjetivo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return montoActual.divide(montoObjetivo, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public boolean estaCompletada() {
        return estado.estaCompletada();
    }

    public boolean estaActiva() {
        return estado.estaActiva();
    }

    // Validaciones de dominio
    private void validarMontoObjetivo() {
        if (montoObjetivo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto objetivo debe ser mayor a cero");
        }
    }

    private void validarNombre() {
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 200) {
            throw new IllegalArgumentException("El nombre no puede exceder 200 caracteres");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getMontoObjetivo() {
        return montoObjetivo;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public LocalDate getFechaObjetivo() {
        return fechaObjetivo;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public EstadoMeta getEstado() {
        return estado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    // Setters (solo para infraestructura)
    protected void setId(Long id) {
        this.id = id;
    }

    protected void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    protected void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    protected void setMontoObjetivo(BigDecimal montoObjetivo) {
        this.montoObjetivo = montoObjetivo;
    }

    protected void setMontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    protected void setFechaObjetivo(LocalDate fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    protected void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    protected void setEstado(EstadoMeta estado) {
        this.estado = estado;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaFinanciera that = (MetaFinanciera) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "MetaFinanciera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", progreso=" + calcularPorcentajeProgreso() + "%" +
                '}';
    }
}

