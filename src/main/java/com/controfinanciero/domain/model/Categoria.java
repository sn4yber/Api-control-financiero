package com.controfinanciero.domain.model;
import com.controfinanciero.domain.model.enums.TipoCategoria;

import java.time.LocalDateTime;
import java.util.Objects;

public class Categoria {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String descripcion;
    private String colorHex;
    private String icono;
    private TipoCategoria tipoCategoria;
    private boolean activa;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío requerido
    protected Categoria() {
    }
    public Categoria(Long usuarioId, String nombre, TipoCategoria tipoCategoria) {
        this.usuarioId = Objects.requireNonNull(usuarioId, "UsuarioId no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.tipoCategoria = Objects.requireNonNull(tipoCategoria, "TipoCategoria no puede ser null");
        this.activa = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validarNombre();
    }
    public void actualizarInformacion(String nombre, String descripcion) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser null");
        this.descripcion = descripcion;
        this.updatedAt = LocalDateTime.now();
        validarNombre();
    }
    public void cambiarApariencia(String colorHex, String icono) {
        this.colorHex = colorHex;
        this.icono = icono;
        this.updatedAt = LocalDateTime.now();
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

    public boolean esDeGasto() {
        return tipoCategoria.esGasto();
    }

    public boolean esDeAhorro() {
        return tipoCategoria.esAhorro();
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

    public String getColorHex() {
        return colorHex;
    }

    public String getIcono() {
        return icono;
    }

    public TipoCategoria getTipoCategoria() {
        return tipoCategoria;
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

    protected void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    protected void setIcono(String icono) {
        this.icono = icono;
    }

    protected void setTipoCategoria(TipoCategoria tipoCategoria) {
        this.tipoCategoria = tipoCategoria;
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
        Categoria categoria = (Categoria) o;
        return id != null && Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipoCategoria +
                '}';

}

}
