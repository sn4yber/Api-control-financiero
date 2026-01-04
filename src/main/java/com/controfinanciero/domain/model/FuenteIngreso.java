package com.controfinanciero.domain.model;

import com.controfinanciero.domain.model.enums.TipoFuente;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio: Fuente de Ingreso.
 * Describe de dónde proviene el dinero del usuario.
 * Permite diferenciar entre ingresos reales y préstamos.
 */
public class FuenteIngreso {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private TipoFuente tipoFuente;
    private boolean esIngresoReal;
    private boolean activa;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío requerido
    protected FuenteIngreso() {
    }

    // Constructor para creación
    public FuenteIngreso(Long usuarioId, String nombre, TipoFuente tipoFuente) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "UsuarioId no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.tipoFuente = Objects.requireNonNull(tipoFuente, "TipoFuente no puede ser null");
        this.esIngresoReal = tipoFuente.esIngresoReal();
        this.activa = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validarNombre();
    }

    // Métodos de negocio
    public void actualizarInformacion(String nombre, String descripcion) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.descripcion = descripcion;
        this.updatedAt = LocalDateTime.now();
        validarNombre();
    }

    public void activar() {
        this.activa = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void desactivar() {
        this.activa = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean estaActiva() {
        return this.activa;
    }

    public boolean esPrestamo() {
        return tipoFuente.esPrestamo();
    }

    // Validaciones de dominio
    private void validarNombre() {
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
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

    public TipoFuente getTipoFuente() {
        return tipoFuente;
    }

    public boolean isEsIngresoReal() {
        return esIngresoReal;
    }

    public boolean isActiva() {
        return activa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
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

    protected void setTipoFuente(TipoFuente tipoFuente) {
        this.tipoFuente = tipoFuente;
    }

    protected void setEsIngresoReal(boolean esIngresoReal) {
        this.esIngresoReal = esIngresoReal;
    }

    protected void setActiva(boolean activa) {
        this.activa = activa;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FuenteIngreso that = (FuenteIngreso) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "FuenteIngreso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipoFuente +
                '}';
    }
}

