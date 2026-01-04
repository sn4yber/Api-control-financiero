package com.controfinanciero.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio: Usuario.
 * Representa a una persona que usa el sistema de control financiero.
 * Cada usuario tiene su propio contexto financiero independiente.
 */
public class Usuario {

    private Long id;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean active;

    // Constructor vacío requerido
    protected Usuario() {
    }

    // Constructor para creación
    public Usuario(String username, String email, String passwordHash, String fullName) {
        this.username = Objects.requireNonNull(username, "Username no puede ser null");
        this.email = Objects.requireNonNull(email, "Email no puede ser null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "Password no puede ser null");
        this.fullName = fullName;
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        validarUsername();
        validarEmail();
    }

    // Métodos de negocio
    public void actualizarInformacion(String fullName) {
        this.fullName = fullName;
        this.updatedAt = LocalDateTime.now();
    }

    public void cambiarPassword(String nuevoPasswordHash) {
        this.passwordHash = Objects.requireNonNull(nuevoPasswordHash, "Password no puede ser null");
        this.updatedAt = LocalDateTime.now();
    }

    public void activar() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void desactivar() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean estaActivo() {
        return this.active;
    }

    // Validaciones de dominio
    private void validarUsername() {
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username no puede estar vacío");
        }
        if (username.length() < 3 || username.length() > 50) {
            throw new IllegalArgumentException("Username debe tener entre 3 y 50 caracteres");
        }
    }

    private void validarEmail() {
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email debe ser válido");
        }
        if (email.length() > 100) {
            throw new IllegalArgumentException("Email no puede exceder 100 caracteres");
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    // Setters (solo para infraestructura)
    protected void setId(Long id) {
        this.id = id;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    protected void setFullName(String fullName) {
        this.fullName = fullName;
    }

    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    // Equals y HashCode basados en ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id != null && Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}

